package pl.tscript3r.ner.fx.dispatch.event;

public class ImportEvent extends AbstractEvent<ImportEventType> {

    private ImportEvent(ImportEventType source) {
        super(source);
    }

    public static ImportEvent get(ImportEventType importEventType) {
        return new ImportEvent(importEventType);
    }

}
