# Deployment Guide

## Overview

This guide covers the deployment process for ReelNet, including both manual and automated deployment options.

## Deployment Architecture

```
[GitHub] -> [CI/CD Pipeline] -> [Container Registry] -> [Kubernetes Cluster]
                                                            │
                                    ┌─────────────────┬─────┴─────┬──────────────┐
                                    │                 │           │              │
                              [Frontend]         [Backend]    [Database]    [Storage]
                                    │                 │           │              │
                              [CloudFront]     [Load Balancer]   │              │
                                    │                 │           │              │
                                    └────────────────>│           │              │
                                                     └───────────>│              │
                                                                 └──────────────>│
```

## Prerequisites

- AWS Account with appropriate permissions
- Domain name and SSL certificates
- Docker Hub or AWS ECR access
- Kubernetes cluster (EKS)
- Terraform installed
- AWS CLI configured

## Infrastructure Setup

### 1. Initialize Terraform

```bash
cd infrastructure
terraform init
```

### 2. Configure Variables

Create `terraform.tfvars`:

```hcl
# AWS Configuration
aws_region = "us-west-2"
environment = "production"
project_name = "reelnet"

# VPC Configuration
vpc_cidr = "10.0.0.0/16"
availability_zones = ["us-west-2a", "us-west-2b", "us-west-2c"]

# EKS Configuration
eks_version = "1.24"
node_instance_type = "t3.medium"
min_nodes = 2
max_nodes = 5

# RDS Configuration
db_instance_class = "db.t3.medium"
db_storage = 100

# Redis Configuration
redis_node_type = "cache.t3.medium"
redis_num_cache_nodes = 2
```

### 3. Apply Infrastructure

```bash
terraform plan
terraform apply
```

## Container Registry Setup

### 1. Create ECR Repositories

```bash
# Create repositories
aws ecr create-repository --repository-name reelnet-backend
aws ecr create-repository --repository-name reelnet-frontend

# Login to ECR
aws ecr get-login-password --region us-west-2 | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.us-west-2.amazonaws.com
```

### 2. Build and Push Images

```bash
# Backend
cd backend
docker build -t reelnet-backend .
docker tag reelnet-backend:latest $AWS_ACCOUNT_ID.dkr.ecr.us-west-2.amazonaws.com/reelnet-backend:latest
docker push $AWS_ACCOUNT_ID.dkr.ecr.us-west-2.amazonaws.com/reelnet-backend:latest

# Frontend
cd frontend
docker build -t reelnet-frontend .
docker tag reelnet-frontend:latest $AWS_ACCOUNT_ID.dkr.ecr.us-west-2.amazonaws.com/reelnet-frontend:latest
docker push $AWS_ACCOUNT_ID.dkr.ecr.us-west-2.amazonaws.com/reelnet-frontend:latest
```

## Kubernetes Deployment

### 1. Configure kubectl

```bash
aws eks update-kubeconfig --name reelnet-cluster --region us-west-2
```

### 2. Apply Kubernetes Manifests

```bash
# Create namespaces
kubectl apply -f k8s/namespaces.yaml

# Apply configurations
kubectl apply -f k8s/config/

# Deploy applications
kubectl apply -f k8s/deployments/
```

### 3. Verify Deployment

```bash
# Check pods
kubectl get pods -n reelnet

# Check services
kubectl get services -n reelnet

# Check ingress
kubectl get ingress -n reelnet
```

## CI/CD Pipeline

### GitHub Actions Workflow

```yaml
name: Deploy to Production

on:
  push:
    branches: [ main ]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2

      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v1
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: us-west-2

      - name: Login to Amazon ECR
        id: login-ecr
        uses: aws-actions/amazon-ecr-login@v1

      - name: Build and push backend
        env:
          ECR_REGISTRY: ${{ steps.login-ecr.outputs.registry }}
          ECR_REPOSITORY: reelnet-backend
          IMAGE_TAG: ${{ github.sha }}
        run: |
          cd backend
          docker build -t $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG .
          docker push $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG

      - name: Build and push frontend
        env:
          ECR_REGISTRY: ${{ steps.login-ecr.outputs.registry }}
          ECR_REPOSITORY: reelnet-frontend
          IMAGE_TAG: ${{ github.sha }}
        run: |
          cd frontend
          docker build -t $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG .
          docker push $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG

      - name: Deploy to EKS
        run: |
          aws eks update-kubeconfig --name reelnet-cluster --region us-west-2
          kubectl set image deployment/backend backend=$ECR_REGISTRY/reelnet-backend:${{ github.sha }} -n reelnet
          kubectl set image deployment/frontend frontend=$ECR_REGISTRY/reelnet-frontend:${{ github.sha }} -n reelnet
```

## Database Migration

### 1. Create Migration Job

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: db-migration
spec:
  template:
    spec:
      containers:
      - name: flyway
        image: flyway/flyway
        args:
        - migrate
        env:
        - name: FLYWAY_URL
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: url
        - name: FLYWAY_USER
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: username
        - name: FLYWAY_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: password
      restartPolicy: Never
```

### 2. Run Migration

```bash
kubectl apply -f k8s/jobs/db-migration.yaml
```

## SSL/TLS Setup

### 1. Install cert-manager

```bash
kubectl apply -f https://github.com/jetstack/cert-manager/releases/download/v1.7.1/cert-manager.yaml
```

### 2. Configure Certificate

```yaml
apiVersion: cert-manager.io/v1
kind: Certificate
metadata:
  name: reelnet-tls
  namespace: reelnet
spec:
  secretName: reelnet-tls
  issuerRef:
    name: letsencrypt-prod
    kind: ClusterIssuer
  dnsNames:
  - api.reelnet.com
  - www.reelnet.com
```

## Monitoring Setup

### 1. Install Prometheus and Grafana

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

helm install prometheus prometheus-community/kube-prometheus-stack \
  --namespace monitoring \
  --create-namespace
```

### 2. Configure Dashboards

Import dashboard configurations:
```bash
kubectl apply -f k8s/monitoring/dashboards/
```

## Backup Strategy

### 1. Database Backups

```bash
# Create backup cronjob
kubectl apply -f k8s/cronjobs/db-backup.yaml

# Verify backup schedule
kubectl get cronjobs -n reelnet
```

### 2. Application Data Backups

Configure S3 lifecycle policies for backup retention.

## Security Measures

### 1. Network Policies

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: backend-policy
  namespace: reelnet
spec:
  podSelector:
    matchLabels:
      app: backend
  ingress:
  - from:
    - podSelector:
        matchLabels:
          app: frontend
    ports:
    - protocol: TCP
      port: 8080
```

### 2. Pod Security Policies

```yaml
apiVersion: policy/v1beta1
kind: PodSecurityPolicy
metadata:
  name: restricted
spec:
  privileged: false
  seLinux:
    rule: RunAsAny
  runAsUser:
    rule: MustRunAsNonRoot
  fsGroup:
    rule: RunAsAny
  volumes:
  - 'configMap'
  - 'emptyDir'
  - 'secret'
```

## Rollback Procedures

### 1. Application Rollback

```bash
# Rollback deployment
kubectl rollout undo deployment/backend -n reelnet
kubectl rollout undo deployment/frontend -n reelnet

# Verify rollback
kubectl rollout status deployment/backend -n reelnet
kubectl rollout status deployment/frontend -n reelnet
```

### 2. Database Rollback

```bash
# Apply flyway rollback
kubectl apply -f k8s/jobs/db-rollback.yaml
```

## Performance Optimization

### 1. Resource Limits

```yaml
resources:
  limits:
    cpu: "1"
    memory: "1Gi"
  requests:
    cpu: "500m"
    memory: "512Mi"
```

### 2. Horizontal Pod Autoscaling

```yaml
apiVersion: autoscaling/v2beta1
kind: HorizontalPodAutoscaler
metadata:
  name: backend-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: backend
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      targetAverageUtilization: 80
```

## Maintenance Procedures

### 1. Regular Updates

```bash
# Update dependencies
./mvnw versions:update-properties
npm update

# Update infrastructure
terraform apply

# Update Kubernetes components
kubectl apply -f k8s/
```

### 2. Monitoring and Alerts

Configure alerts in Grafana for:
- High CPU/Memory usage
- Error rate spikes
- Slow response times
- Disk space usage

## Troubleshooting

### Common Issues

1. **Pod Crashes**
   ```bash
   kubectl describe pod <pod-name> -n reelnet
   kubectl logs <pod-name> -n reelnet
   ```

2. **Database Connection Issues**
   ```bash
   kubectl exec -it <pod-name> -n reelnet -- pg_isready -h $DB_HOST
   ```

3. **Network Issues**
   ```bash
   kubectl exec -it <pod-name> -n reelnet -- curl backend-service:8080/health
   ```

For more information about deployment and infrastructure, refer to:
- [Infrastructure Documentation](../architecture/infrastructure.md)
- [Security Documentation](./security.md)
- [Monitoring Guide](./monitoring.md) 