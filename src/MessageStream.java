import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MessageStream<T> {
	private StreamWrapper<T> headerWrapper;
	private StreamWrapper<?> tailWrapper;

	private MessageStream<T> saveWrapper(StreamWrapper<?> wrapper) {
		if (null == headerWrapper) {
			headerWrapper = (StreamWrapper<T>) wrapper;
			tailWrapper = headerWrapper;
		} else {
			tailWrapper.setNext(wrapper);
			tailWrapper = wrapper;
		}

		return this;
	}

	public <E> MessageStream<T> filter(Predicate<E> predicate) {
		return saveWrapper(new StreamFilterWrapper<E>(predicate));
	}

	public <E, R> MessageStream<T> map(Function<E, R> map) {
		return saveWrapper(new StreamMapWrapper<E, R>(map));
	}

	public <E> void forEach(Consumer<E> consumer) {
		saveWrapper(new StreamConsumerWrapper<E>(consumer));
	}

	public Object accept(Stream<T> stream) {
		return headerWrapper.apply(stream);
	}

	private static abstract class StreamWrapper<T> {
		public abstract Object apply(Stream<T> stream);

		public abstract StreamWrapper<?> setNext(StreamWrapper<?> next);
	}

	private static class StreamFilterWrapper<T> extends StreamWrapper<T> {

		public StreamWrapper<T> _next;
		private Predicate<T> _pPredicate;

		public StreamFilterWrapper(Predicate<T> predicate) {
			_pPredicate = predicate;
		}

		@Override
		public Object apply(Stream<T> stream) {
			// TODO Auto-generated method stub
			return _next.apply(stream.filter(_pPredicate));
		}

		@Override
		public StreamWrapper<?> setNext(StreamWrapper<?> next) {
			// TODO Auto-generated method stub
			_next = (StreamWrapper<T>) next;
			return _next;
		}
	}

	private static class StreamMapWrapper<T, R> extends StreamWrapper<T> {
		public StreamWrapper<R> _next;
		private Function<T, R> _map;

		public StreamMapWrapper(Function<T, R> function) {
			_map = function;
		}

		@Override
		public Object apply(Stream<T> stream) {
			// TODO Auto-generated method stub
			return _next.apply(stream.map(_map));
		}

		@Override
		public StreamWrapper<?> setNext(StreamWrapper<?> next) {
			// TODO Auto-generated method stub
			_next = (StreamWrapper<R>) next;
			return _next;
		}
	}

	private static class StreamConsumerWrapper<T> extends StreamWrapper<T> {
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
		public StreamWrapper<?> setNext(StreamWrapper<?> next) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
