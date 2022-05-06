package com.goody.utils.baihao.valuechecker;

import org.springframework.stereotype.Service;


/**
 * example for service of {@link ValueCheckers}
 *
 * @author Goody
 * @version 1.0, 2021/3/26
 * @since 1.0.0
 */
@Service
public class TargetService {

    @ValueCheckers(checkers = {
            @ValueCheckers.ValueChecker(method = "verify", keys = {"#id", "#name"}, handler = SampleCheckerHandlerImpl.class),
            @ValueCheckers.ValueChecker(method = "verify", keys = "#id", handler = SampleCheckerHandlerImpl.class),
            @ValueCheckers.ValueChecker(method = "verify", keys = "#name", handler = SampleCheckerHandlerImpl.class)
    })
    public void checker(Long id, String name) {
    }

    @ValueCheckers(checkers = {
            @ValueCheckers.ValueChecker(method = "verify", keys = {"#node.id", "#node.name"}, handler = SampleCheckerHandlerImpl.class),
            @ValueCheckers.ValueChecker(method = "verify", keys = "#node.id", handler = SampleCheckerHandlerImpl.class),
            @ValueCheckers.ValueChecker(method = "verify", keys = "#node.name", handler = SampleCheckerHandlerImpl.class)
    })
    public void checker(Node node) {
    }

    @ValueCheckers(checkers = {
            @ValueCheckers.ValueChecker(method = "verify", keys = "#id", handler = SampleCheckerHandlerImpl.class)
    })
    public void checker(Long id) {
    }

    @ValueCheckers(checkers = {
            @ValueCheckers.ValueChecker(method = "verify", keys = "#name", handler = SampleCheckerHandlerImpl.class)
    })
    public void checker(String name) {
    }

    @ValueCheckers(checkers = {
            @ValueCheckers.ValueChecker(method = "verify", handler = SampleCheckerHandlerImpl.class)
    })
    public void checker() {
    }

    @ValueCheckers(checkers = {
            @ValueCheckers.ValueChecker(method = "verifyError", handler = SampleCheckerHandlerImpl.class)
    })
    public void checkerError() {
    }

    public static class Node {
        private Long id;
        private String name;

        public Node() {

        }

        public Node(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
