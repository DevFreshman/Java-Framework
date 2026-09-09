package org.example.javaframework.infra.security;

public final class CurrentUserContext {

    private static final ThreadLocal<UserSession> holder = new ThreadLocal<>();

    private CurrentUserContext() {
    }

    public static void set(UserSession session) {
        holder.set(session);
    }

    public static UserSession get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }
}
