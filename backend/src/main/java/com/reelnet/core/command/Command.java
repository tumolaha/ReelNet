package com.reelnet.core.command;

public interface Command<T> {
    T execute();
} 