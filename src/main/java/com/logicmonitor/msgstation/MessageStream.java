package com.logicmonitor.msgstation;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MessageStream<T> {
	private StreamWrapper<T, ?> wrapper;
	private MessageStream<?> previous;

	public MessageStream<T> filter(Predicate<? super T> predicate) {
		StreamWrapper<T, T> sw = new StreamFilterWrapper<>(predicate);
		linkWrapper(sw);

		return createStream();
	}

	public <R> MessageStream<R> map(Function<? super T, ? extends R> map) {
		StreamWrapper<T, R> sw = new StreamMapWrapper<>(map);
		linkWrapper(sw);

		return createStream();
	}

	public void forEach(Consumer<? super T> consumer) {
		StreamWrapper<T, Void> sw = new StreamConsumerWrapper<>(consumer);
		linkWrapper(sw);
	}

	public Object accept(Stream<T> stream) {
		return wrapper.apply(stream);
	}

	private <R> MessageStream<R> createStream() {
		MessageStream<R> mStream = new MessageStream<>();
		mStream.previous = this;

		return mStream;
	}

	private void linkWrapper(StreamWrapper<T, ?> sw) {
        if (null != wrapper) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }

		wrapper = sw;

		if (null != previous) {
			previous.wrapper.next(wrapper);
		}
	}

	private static abstract class StreamWrapper<E_IN, E_OUT> {
        protected abstract Object apply(Stream<E_IN> stream);

        protected abstract void next(StreamWrapper<?, ?> next);
	}

	private static class StreamFilterWrapper<T> extends StreamWrapper<T, T> {

		private StreamWrapper<T, ?> _next;
		private Predicate<? super T> _pPredicate;

        private StreamFilterWrapper(Predicate<? super T> predicate) {
			_pPredicate = predicate;
		}

		@Override
        protected Object apply(Stream<T> stream) {
			return _next.apply(stream.filter(_pPredicate));
		}

		@SuppressWarnings("unchecked")
		@Override
        protected void next(StreamWrapper<?, ?> next) {
			_next = (StreamWrapper<T, ?>) next;
		}
	}

	private static class StreamMapWrapper<T, R> extends StreamWrapper<T, R> {
		private StreamWrapper<R, ?> _next;
		private Function<? super T, ? extends R> _map;

        private StreamMapWrapper(Function<? super T, ? extends R> function) {
			_map = function;
		}

		@Override
        protected Object apply(Stream<T> stream) {
			return _next.apply(stream.map(_map));
		}

		@SuppressWarnings("unchecked")
		@Override
        protected void next(StreamWrapper<?, ?> next) {
			_next = (StreamWrapper<R, ?>) next;
		}
	}

	private static class StreamConsumerWrapper<T> extends StreamWrapper<T, Void> {
		private Consumer<? super T> _consumer;

        private StreamConsumerWrapper(Consumer<? super T> consumer) {
			_consumer = consumer;
		}

		@Override
        protected Object apply(Stream<T> stream) {
			stream.forEach(_consumer);
			return null;
		}

		@Override
        protected void next(StreamWrapper<?, ?> next) {
		}
	}
}
