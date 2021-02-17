package pl.tscript3r.ner.fx.dispatch.event;

public class FormEvent extends AbstractEvent<FormEventType> {

    private FormEvent(FormEventType source) {
        super(source);
    }

    public static FormEvent get(FormEventType formEventType) {
        return new FormEvent(formEventType);
    }

}
