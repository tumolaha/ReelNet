import { baseApi } from "@/core/api/baseApi";

interface Post {
  id: number;
  title: string;
  content: string;
  userId: number;
}

interface CreatePostDto {
  title: string;
  content: string;
}

export const postsApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getPosts: builder.query<Post[], void>({
      query: () => ({ url: "posts", method: "GET" }),
      providesTags: ["Posts"],
    }),

    getPost: builder.query<Post, number>({
      query: (id) => ({ url: `posts/${id}`, method: "GET" }),
      providesTags: (_result, _error, id) => [{ type: "Posts", id }],
    }),

    createPost: builder.mutation<Post, CreatePostDto>({
      query: (newPost) => ({
        url: "posts",
        method: "POST",
        body: newPost,
      }),
      invalidatesTags: ["Posts"],
    }),

    updatePost: builder.mutation<Post, Partial<Post> & Pick<Post, "id">>({
      query: ({ id, ...patch }) => ({
        url: `posts/${id}`,
        method: "PATCH",
        body: patch,
      }),
      invalidatesTags: (_result, _error, { id }) => [{ type: "Posts", id }],
    }),

    deletePost: builder.mutation<void, number>({
      query: (id) => ({
        url: `posts/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: ["Posts"],
    }),
  }),
});

// Export hooks for usage in components
export const {
  useGetPostsQuery,
  useGetPostQuery,
  useCreatePostMutation,
  useUpdatePostMutation,
  useDeletePostMutation,
} = postsApi;
