import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MessageStream<T> {
	private StreamWrapper<T, ?> wrapper;
	private MessageStream<?> previous;

	public MessageStream<T> filter(Predicate<T> predicate) {
		StreamWrapper<T, T> sw = new StreamFilterWrapper<T>(predicate);
		linkWrapper(sw);

		return createStream(sw);
	}

	public <R> MessageStream<R> map(Function<T, R> map) {
		StreamWrapper<T, R> sw = new StreamMapWrapper<T, R>(map);
		linkWrapper(sw);

		return createStream(sw);
	}

	public void forEach(Consumer<T> consumer) {
		StreamWrapper<T, Void> sw = new StreamConsumerWrapper<T>(consumer);
		linkWrapper(sw);
	}

	public Object accept(Stream<T> stream) {
		return wrapper.apply(stream);
	}

	private <R> MessageStream<R> createStream(StreamWrapper<T, R> func) {
		MessageStream<R> mStream = new MessageStream<>();
		mStream.previous = this;

		return mStream;
	}

	private void linkWrapper(StreamWrapper<T, ?> sw) {
		wrapper = sw;

		if (null != previous) {
			previous.wrapper.next(wrapper);
		}
	}

	private static abstract class StreamWrapper<E_IN, E_OUT> {
		public abstract Object apply(Stream<E_IN> stream);

		public abstract void next(StreamWrapper<?, ?> next);
	}

	private static class StreamFilterWrapper<T> extends StreamWrapper<T, T> {

		public StreamWrapper<T, ?> _next;
		private Predicate<T> _pPredicate;

		public StreamFilterWrapper(Predicate<T> predicate) {
			_pPredicate = predicate;
		}

		@Override
		public Object apply(Stream<T> stream) {
			// TODO Auto-generated method stub
			return _next.apply(stream.filter(_pPredicate));
		}

		@SuppressWarnings("unchecked")
		@Override
		public void next(StreamWrapper<?, ?> next) {
			// TODO Auto-generated method stub
			_next = (StreamWrapper<T, ?>) next;
		}
	}

	private static class StreamMapWrapper<T, R> extends StreamWrapper<T, R> {
		public StreamWrapper<R, ?> _next;
		private Function<T, R> _map;

		public StreamMapWrapper(Function<T, R> function) {
			_map = function;
		}

		@Override
		public Object apply(Stream<T> stream) {
			// TODO Auto-generated method stub
			return _next.apply(stream.map(_map));
		}

		@SuppressWarnings("unchecked")
		@Override
		public void next(StreamWrapper<?, ?> next) {
			// TODO Auto-generated method stub
			_next = (StreamWrapper<R, ?>) next;
		}
	}

	private static class StreamConsumerWrapper<T> extends StreamWrapper<T, Void> {
		private Consumer<T> _consumer;

		public StreamConsumerWrapper(Consumer<T> consumer) {
			_consumer = consumer;
		}

		@Override
		public Object apply(Stream<T> stream) {
			// TODO Auto-generated method stub
			stream.forEach(_consumer);
			return null;
		}

		@Override
		public void next(StreamWrapper<?, ?> next) {
			// TODO Auto-generated method stub
		}
	}
}
