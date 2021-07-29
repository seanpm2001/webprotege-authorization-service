package edu.stanford.protege.webprotege.authorization;


import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Jan 2017
 *
 * Represents the subject of a role assignment.  The subject can represent a specific signed in user,
 * the guest user, or any signed in user.
 */
public abstract class Subject {


    private Subject() {
    }

    /**
     * Determines whether this subject identifies the guest user.
     * @return {@code true} if this subject identifies the guest user, otherwise {@code false}.
     */
    public abstract boolean isGuest();

    /**
     * Determines whether this subject identifies any signed in user (i.e. a user that is not the guest user)
     * @return {@code true} if this subject identifies any signed in user, otherwise {@code false}.
     */
    public abstract boolean isAnySignedInUser();

    /**
     * Gets the user name that this subject identifies.
     * @return The optional user name.  An empty value will be returned if this subject identifies any
     * user (i.e. a specific user or the guest user).
     */
    @Nonnull
    public abstract Optional<String> getUserName();

    public abstract Optional<UserId> getUserId();

    public static Subject forAnySignedInUser() {
        return AnySignedInUser.INSTANCE;
    }

    public static Subject forUser(@Nonnull UserId userId) {
        return new SpecificUser(userId);
    }

    public static Subject forUser(@Nonnull String userId) {
        return forUser(UserId.valueOf(userId));
    }

    public static Subject forGuestUser() {
        return SpecificUser.GUEST;
    }


    private static class AnySignedInUser extends Subject {

        private static final AnySignedInUser INSTANCE = new AnySignedInUser();

        @Override
        public boolean isGuest() {
            return false;
        }

        @Override
        public boolean isAnySignedInUser() {
            return true;
        }

        @Nonnull
        @Override
        public Optional<String> getUserName() {
            return Optional.empty();
        }

        @Override
        public Optional<UserId> getUserId() {
            return Optional.empty();
        }

        @Override
        public int hashCode() {
            return 33;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof AnySignedInUser;
        }

        @Override
        public String toString() {
            return "AnySignedInUser{}";
        }
    }

    private static class SpecificUser extends Subject {

        private static final SpecificUser GUEST = new SpecificUser(UserId.getGuest());

        private final UserId userId;

        public SpecificUser(UserId userId) {
            this.userId = Objects.requireNonNull(userId);
        }

        @Override
        public boolean isGuest() {
            return userId.isGuest();
        }

        @Nonnull
        @Override
        public Optional<String> getUserName() {
            return Optional.of(userId.id());
        }

        @Override
        public Optional<UserId> getUserId() {
            return Optional.of(userId);
        }

        @Override
        public boolean isAnySignedInUser() {
            return false;
        }

        @Override
        public int hashCode() {
            return userId.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof SpecificUser other)) {
                return false;
            }
            return this.userId.equals(other.userId);
        }

        @Override
        public String toString() {
            return "SpecificUser{" + "userId=" + userId + '}';
        }
    }

}
