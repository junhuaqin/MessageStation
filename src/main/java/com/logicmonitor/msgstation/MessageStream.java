package com.logicmonitor.msgstation;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MessageStream<T>{
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

	public MessageStream<T> peek(Consumer<? super T> consumer) {
		StreamWrapper<T, T> sw = new StreamPeekWrapper<>(consumer);
		linkWrapper(sw);

		return createStream();
	}

	public void forEach(Consumer<? super T> consumer) {
		StreamWrapper<T, Void> sw = new StreamForeachWrapper<>(consumer);
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
			((StreamWrapper<?, T>)previous.wrapper).next(wrapper);
		}
	}

	private static abstract class StreamWrapper<E_IN, E_OUT> {
		protected StreamWrapper<E_OUT, ?> _next;

        protected abstract Object apply(Stream<E_IN> stream);

        protected void next(StreamWrapper<E_OUT, ?> next){
			_next = next;
		}
	}

	private static class StreamFilterWrapper<T> extends StreamWrapper<T, T> {
		private Predicate<? super T> _pPredicate;

        private StreamFilterWrapper(Predicate<? super T> predicate) {
			_pPredicate = predicate;
		}

		@Override
        protected Object apply(Stream<T> stream) {
			return _next.apply(stream.filter(_pPredicate));
		}
	}

	private static class StreamMapWrapper<T, R> extends StreamWrapper<T, R> {
		private Function<? super T, ? extends R> _map;

        private StreamMapWrapper(Function<? super T, ? extends R> function) {
			_map = function;
		}

		@Override
        protected Object apply(Stream<T> stream) {
			return _next.apply(stream.map(_map));
		}
	}

	private static class StreamPeekWrapper<T> extends StreamWrapper<T, T> {
		private Consumer<? super T> _consumer;

		private StreamPeekWrapper(Consumer<? super T> consumer) {
			_consumer = consumer;
		}

		@Override
		protected Object apply(Stream<T> stream) {
			return _next.apply(stream.peek(_consumer));
		}
	}

	private static class StreamForeachWrapper<T> extends StreamWrapper<T, Void> {
		private Consumer<? super T> _consumer;

        private StreamForeachWrapper(Consumer<? super T> consumer) {
			_consumer = consumer;
		}

		@Override
        protected Object apply(Stream<T> stream) {
			stream.forEach(_consumer);
			return null;
		}
	}
}
