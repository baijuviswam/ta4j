package eu.verdelhan.tailtest.analysis.criteria;

import java.util.LinkedList;
import java.util.List;

import eu.verdelhan.tailtest.AnalysisCriterion;
import eu.verdelhan.tailtest.OperationType;
import eu.verdelhan.tailtest.TimeSeries;
import eu.verdelhan.tailtest.Trade;
import eu.verdelhan.tailtest.analysis.evaluator.Decision;

public class AverageProfitableTradesCriterion implements AnalysisCriterion {

    @Override
    public double calculate(TimeSeries series, Trade trade) {
        if (trade.getEntry().getType() == OperationType.BUY) {
            return (series.getTick(trade.getExit().getIndex()).getClosePrice() / series.getTick(
                    trade.getEntry().getIndex()).getClosePrice()) > 1d ? 1d : 0d;
        } else {
            return (series.getTick(trade.getEntry().getIndex()).getClosePrice() / series.getTick(
                    trade.getExit().getIndex()).getClosePrice()) > 1d ? 1d : 0d;
        }
    }

    @Override
    public double calculate(TimeSeries series, List<Trade> trades) {
        int numberOfProfitable = 0;
        for (Trade trade : trades) {
            if (trade.getEntry().getType() == OperationType.BUY) {
                if ((series.getTick(trade.getExit().getIndex()).getClosePrice() / series.getTick(
                        trade.getEntry().getIndex()).getClosePrice()) > 1d) {
                    numberOfProfitable++;
                }
            } else if ((series.getTick(trade.getEntry().getIndex()).getClosePrice() / series.getTick(
                    trade.getExit().getIndex()).getClosePrice()) > 1d) {
                numberOfProfitable++;
            }
        }
        return (double) numberOfProfitable / trades.size();
    }

    @Override
    public String getName() {
        return "Average Profitable Trades";
    }

    @Override
    public double summarize(TimeSeries series, List<Decision> decisions) {
        List<Trade> trades = new LinkedList<Trade>();

        for (Decision decision : decisions) {
            trades.addAll(decision.getTrades());
        }
        return calculate(series, trades);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (this.getClass().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }

}
