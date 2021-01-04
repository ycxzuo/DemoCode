package com.yczuoxin.concurrent.demo.basis.future.completable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompletableFutureTest {

    public void test() {
        CompletableFuture<List<String>> ids = new CompletableFuture<>();

        CompletableFuture<List<String>> result = ids.thenComposeAsync(l -> { // <2>
            Stream<CompletableFuture<String>> zip =
                    l.stream().map(i -> { // <3>
                        CompletableFuture<String> nameTask = ifhName(i); // <4>
                        CompletableFuture<Integer> statTask = ifhStat(i); // <5>

                        return nameTask.thenCombineAsync(statTask, (name, stat) -> "Name " + name + " has stats " + stat); // <6>
                    });
            List<CompletableFuture<String>> combinationList = zip.collect(Collectors.toList()); // <7>
            CompletableFuture<String>[] combinationArray = combinationList.toArray(new CompletableFuture[combinationList.size()]);

            CompletableFuture<Void> allDone = CompletableFuture.allOf(combinationArray); // <8>
            return allDone.thenApply(v -> combinationList.stream()
                    .map(CompletableFuture::join) // <9>
                    .collect(Collectors.toList()));
        });
    }

    private CompletableFuture<Integer> ifhStat(String i) {
        return null;
    }

    private CompletableFuture<String> ifhName(String i) {
        return null;
    }

}
