package com.waskj.base.chart.core.support;

import com.waskj.base.chart.core.ChartOptionContributor;
import com.waskj.base.chart.core.model.axis.Axis;
import com.waskj.base.chart.core.model.series.Series;
import com.waskj.base.chart.core.model.*;

import java.util.Collection;

/**
 * Created by poet on 2016/12/29.
 */
public abstract class AbstractChartOptionConstributor implements ChartOptionContributor {

    @Override
    public Option contribute() {
        Option option = new Option();

        // 设置标题
        option.title(option.title());
        // 设置是否可计算
        option.calculable(this.contributeCalculable());
        // 设置legend
        option.legend(option.legend());


        return option;
    }

    // 提供 calculable
    protected abstract boolean contributeCalculable();

    protected abstract boolean contributeHoverable();

    protected abstract boolean contributeClickable();

    protected abstract boolean contributeAnimation();

    protected abstract Integer contributeAnimationDuration();

    // 提供 标题
    protected abstract void contributeTitle(Title title);

    // 提供 legend
    protected abstract void contributeLegend(Legend legend);

    // 提供 ToolBox
    protected abstract void contributeToolBox(Toolbox toolbox);

    // 提供ToolTip
    protected abstract void contributeToolTip(Tooltip tooltip);

    // 提供TimeLine
    protected abstract void contributeTimeLine(Timeline timeline);

    // 提供
    protected abstract void contributeDataRange(DataRange dataRange);

    // 提供Grid
    protected abstract void contributeGrid(Grid grid);

    // 提供x轴
    protected abstract void contributeXAxis(Collection<Axis> xAxis);

    // 提供Y周
    protected abstract void contributeYAxis(Collection<Axis> yAxis);

    // 提供Series
    protected abstract void contributeSeries(Collection<Series> series);
}
