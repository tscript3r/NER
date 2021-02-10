package pl.tscript3r.ner.fx.dispatch.event;

public class StageCreationExceptionEvent extends AbstractEvent<Exception> {

    private StageCreationExceptionEvent(Exception source) {
        super(source);
    }

    public static StageCreationExceptionEvent get(Exception e) {
        return new StageCreationExceptionEvent(e);
    }

}
