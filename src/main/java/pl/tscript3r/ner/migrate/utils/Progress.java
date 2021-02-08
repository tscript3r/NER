package pl.tscript3r.ner.migrate.utils;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class Progress {

    private final AtomicInteger totalCount = new AtomicInteger(0);
    private final AtomicInteger current = new AtomicInteger(0);
    private final Consumer<Double> onProgress;

    public void addTotalCount(int totalCount) {
        this.totalCount.set(this.totalCount.get() + totalCount);
    }

    public void inc() {
        current.set(current.get() + 1);
        double percentage = ((double) current.get() / totalCount.get());
        onProgress.accept(percentage);
    }

    public Integer getCurrent() {
        return current.get();
    }

    public Integer getTotalCount() {
        return totalCount.get();
    }

}
