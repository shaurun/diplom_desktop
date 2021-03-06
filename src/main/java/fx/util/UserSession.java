package fx.util;

import fx.model.Lesson;
import fx.model.Subject;
import fx.model.User;

public class UserSession {
    private static UserSession userSession;
    private User user;
    private Subject subject;
    private static Lesson lesson;

    private UserSession() {}

    public static void setUser(User user) {
       if (userSession == null) {
           userSession = new UserSession();
       }

       userSession.user = user;
    }

    public static User getUser() {
        if (userSession == null) {
            userSession = new UserSession();
        }

        return userSession.user;
    }

    public static Subject getSubject() {
        return userSession.subject;
    }

    public static void setSubject(Subject subject) {
        userSession.subject = subject;
    }

    public static Lesson getLesson() {
        return lesson;
    }

    public static void setLesson(Lesson lesson) {
        UserSession.lesson = lesson;
    }
}
