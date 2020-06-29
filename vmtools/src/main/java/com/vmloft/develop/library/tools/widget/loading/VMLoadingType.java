package com.vmloft.develop.library.tools.widget.loading;

/**
 * Create by lzan13 on 2020/6/29 10:45
 * 描述：
 */
public enum VMLoadingType {

    CIRCLE(CircleBuilder.class);
//    CIRCLE_CLOCK(ClockBuilder.class),
//    STAR_LOADING(StarBuilder.class),
//    LEAF_ROTATE(LeafBuilder.class),
//    DOUBLE_CIRCLE(DoubleCircleBuilder.class),
//    PAC_MAN(PacManBuilder.class),
//    ELASTIC_BALL(ElasticBallBuilder.class),
//    INFECTION_BALL(InfectionBallBuilder.class),
//    INTERTWINE(IntertwineBuilder.class),
//    TEXT(TextBuilder.class),
//    SEARCH_PATH(SearchPathBuilder.class),
//    ROTATE_CIRCLE(RotateCircleBuilder.class),
//    SINGLE_CIRCLE(SingleCircleBuilder.class),
//    SNAKE_CIRCLE(SnakeCircleBuilder.class),
//    STAIRS_PATH(StairsPathBuilder.class),
//    MUSIC_PATH(MusicPathBuilder.class),
//    STAIRS_RECT(StairsRectBuilder.class),
//    CHART_RECT(ChartRectBuilder.class);

    private final Class<?> mBuilderClass;

    VMLoadingType(Class<?> builderClass) {
        this.mBuilderClass = builderClass;
    }

    <T extends VMLoadingBuilder> T newInstance() {
        try {
            return (T) mBuilderClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
