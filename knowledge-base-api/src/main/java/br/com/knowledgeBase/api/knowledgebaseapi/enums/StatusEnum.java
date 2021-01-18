package br.com.knowledgeBase.api.knowledgebaseapi.enums;

public enum StatusEnum {
    DRAFT{
        @Override
        public String toString() {
            return "DRAFT";
        }
    },
    PUBLISH{
        @Override
        public String toString() {
            return "PUBLISH";
        }
    },
    CANCEL{
        @Override
        public String toString() {
            return "CANCEL";
        }
    }
}
