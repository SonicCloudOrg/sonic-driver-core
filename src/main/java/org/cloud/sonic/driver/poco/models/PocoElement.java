package org.cloud.sonic.driver.poco.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class PocoElement {
    private Payload payload;
    private List<PocoElement> children;

    @Getter
    @ToString
    @AllArgsConstructor
    public class Payload{
        private String layer;
        private String name;
        private String tag;
        private String text;
        private String texture;
        private Integer _instanceId;
        private Integer _ilayer;
        private String type;
        private Boolean visible;
        private ZOrders zOrders;
        private List<String> components;
        private List<Float> anchorPoint;
        private List<Float> scale;
        private List<Float> size;
        private List<Float> pos;
        private Boolean clickable;

        @Getter
        @ToString
        @AllArgsConstructor
        public class ZOrders{
            private Integer global;
            private Integer local;
        }
    }
}
