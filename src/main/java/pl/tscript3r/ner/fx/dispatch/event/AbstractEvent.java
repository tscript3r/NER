package pl.tscript3r.ner.fx.dispatch.event;

import org.springframework.context.ApplicationEvent;

public abstract class AbstractEvent<T> extends ApplicationEvent {

    public AbstractEvent(T source) {
        super(source);
    }

    public T get() {
        return (T) getSource();
    }

}
