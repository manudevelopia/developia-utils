package info.developia.util;

import java.util.function.Function;

public sealed interface Result<A> {
    record Success<A>(A value) implements Result<A> {
    }

    record Failure<A>(Throwable error) implements Result<A> {
    }

    default A getOr(A fallbackValue) {
        if (this instanceof Success<A> success) {
            return success.value();
        }
        return fallbackValue;
    }

    default A getOrFailWith(RuntimeException exception) {
        if (this instanceof Success<A> success) {
            return success.value();
        }
        throw exception;
    }

    default Throwable fail() {
        if (this instanceof Result.Failure<A> e)
            return e.error();
        else return null;
    }

    default A getOrFailWith(Function<Throwable, RuntimeException> exception) {
        if (this instanceof Result.Failure<A> e) {
            throw exception.apply(e.error());
        }
        return this.get();
    }

    private A get() {
        return ((Success<A>) this).value();
    }

    default <U> Result<U> map(Function<A, U> f) {
        return flatMap(this, s -> Try.of(() -> f.apply(s)));
    }

    private <U> Result<U> flatMap(Result<A> from, Function<A, Result<U>> f) {
        if (from instanceof Result.Success<A> success) {
            return executeF(f, success.value);
        }
        return new Failure<>(this.fail());
    }

    private <U> Result<U> executeF(final Function<A, Result<U>> f, final A value) {
        try {
            return f.apply(value);
        } catch (Throwable e) {
            return failed(e);
        }
    }

    private <U> Result<U> failed(final Throwable error) {
        return new Result.Failure<>(error);
    }
}
