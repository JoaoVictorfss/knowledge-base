package br.com.knowledgeBase.api.knowledgebaseapi.Data.enums;

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
