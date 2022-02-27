package gps_watch.tracking;

import gps_watch.Tracking;

import io.ciera.runtime.api.domain.Message;
import io.ciera.runtime.api.exceptions.ActionException;
import io.ciera.runtime.api.exceptions.PortMessageException;

import tracking.shared.GoalCriteria;
import tracking.shared.GoalSpan;
import tracking.shared.Indicator;
import tracking.shared.Unit;

import ui.shared.IUI;


public class TrackingUI extends SocketPort implements IUI {

    public TrackingUI(Tracking domain) {
        super("TrackingUI", domain);
    }

    // inbound messages
    public void lapResetPressed() {
        getApplication().getLogger().trace("SMT: Enter action: Tracking::Tracking::UI::UI.lapResetPressed");
        int _lineNumber = -1;
        try {
            /* ::lapResetPressed() */
            _lineNumber = 1;
            getDomain().lapResetPressed();
        } catch (RuntimeException _e) {
            if (!(_e instanceof ActionException)) {
                _e = new ActionException(_e);
            }
            ((ActionException) _e).updateStack("Tracking::Tracking::UI::UI", "lapResetPressed", "<Unknown>", _lineNumber);
            throw _e;
        } finally {
            getApplication().getLogger().trace("SMT: Exit action: Tracking::Tracking::UI::UI.lapResetPressed");
        }
    }

    public void lightPressed() {
        getApplication().getLogger().trace("SMT: Enter action: Tracking::Tracking::UI::UI.lightPressed");
        int _lineNumber = -1;
        try {
            /* ::lightPressed() */
            _lineNumber = 1;
            getDomain().lightPressed();
        } catch (RuntimeException _e) {
            if (!(_e instanceof ActionException)) {
                _e = new ActionException(_e);
            }
            ((ActionException) _e).updateStack("Tracking::Tracking::UI::UI", "lightPressed", "<Unknown>", _lineNumber);
            throw _e;
        } finally {
            getApplication().getLogger().trace("SMT: Exit action: Tracking::Tracking::UI::UI.lightPressed");
        }
    }

    public void modePressed() {
        getApplication().getLogger().trace("SMT: Enter action: Tracking::Tracking::UI::UI.modePressed");
        int _lineNumber = -1;
        try {
            /* ::modePressed() */
            _lineNumber = 1;
            getDomain().modePressed();
        } catch (RuntimeException _e) {
            if (!(_e instanceof ActionException)) {
                _e = new ActionException(_e);
            }
            ((ActionException) _e).updateStack("Tracking::Tracking::UI::UI", "modePressed", "<Unknown>", _lineNumber);
            throw _e;
        } finally {
            getApplication().getLogger().trace("SMT: Exit action: Tracking::Tracking::UI::UI.modePressed");
        }
    }

    public void startStopPressed() {
        getApplication().getLogger().trace("SMT: Enter action: Tracking::Tracking::UI::UI.startStopPressed");
        int _lineNumber = -1;
        try {
            /* ::startStopPressed() */
            _lineNumber = 1;
            getDomain().startStopPressed();
        } catch (RuntimeException _e) {
            if (!(_e instanceof ActionException)) {
                _e = new ActionException(_e);
            }
            ((ActionException) _e).updateStack("Tracking::Tracking::UI::UI", "startStopPressed", "<Unknown>", _lineNumber);
            throw _e;
        } finally {
            getApplication().getLogger().trace("SMT: Exit action: Tracking::Tracking::UI::UI.startStopPressed");
        }
    }

    public void newGoalSpec(final GoalSpan p_spanType,  final GoalCriteria p_criteriaType,  final double p_span,  final double p_maximum,  final double p_minimum,  final int p_sequenceNumber) {
        getApplication().getLogger().trace("SMT: Enter action: Tracking::Tracking::UI::UI.newGoalSpec");
        int _lineNumber = -1;
        try {
            /* ::newGoalSpec( spanType:param.spanType, criteriaType:param.criteriaType, span:param.span, maximum:param.maximum, minimum:param.minimum, sequenceNumber:param.sequenceNumber ) */
            _lineNumber = 1;
            getDomain().newGoalSpec(p_spanType, p_criteriaType, p_span, p_maximum, p_minimum, p_sequenceNumber);
        } catch (RuntimeException _e) {
            if (!(_e instanceof ActionException)) {
                _e = new ActionException(_e);
            }
            ((ActionException) _e).updateStack("Tracking::Tracking::UI::UI", "newGoalSpec", "<Unknown>", _lineNumber);
            throw _e;
        } finally {
            getApplication().getLogger().trace("SMT: Exit action: Tracking::Tracking::UI::UI.newGoalSpec");
        }
    }

    public void setTargetPressed() {
        getApplication().getLogger().trace("SMT: Enter action: Tracking::Tracking::UI::UI.setTargetPressed");
        int _lineNumber = -1;
        try {
            /* ::setTargetPressed() */
            _lineNumber = 1;
            getDomain().setTargetPressed();
        } catch (RuntimeException _e) {
            if (!(_e instanceof ActionException)) {
                _e = new ActionException(_e);
            }
            ((ActionException) _e).updateStack("Tracking::Tracking::UI::UI", "setTargetPressed", "<Unknown>", _lineNumber);
            throw _e;
        } finally {
            getApplication().getLogger().trace("SMT: Exit action: Tracking::Tracking::UI::UI.setTargetPressed");
        }
    }


    // outbound messages
    public void setData(final double p_value,  final Unit p_unit) {
        if (satisfied()) {
            if (getPeer() instanceof IUI && getContext().equals(getPeer().getContext())) {
                ((IUI) getPeer()).setData(p_value, p_unit);
            } else {
                send(new IUI.SetData(p_value, p_unit));
            }
        } else {}
    }
    public void setIndicator(final Indicator p_indicator) {
        if (satisfied()) {
            if (getPeer() instanceof IUI && getContext().equals(getPeer().getContext())) {
                ((IUI) getPeer()).setIndicator(p_indicator);
            } else {
                send(new IUI.SetIndicator(p_indicator));
            }
        } else {}
    }
    public void setTime(final int p_time) {
        if (satisfied()) {
            if (getPeer() instanceof IUI && getContext().equals(getPeer().getContext())) {
                ((IUI) getPeer()).setTime(p_time);
            } else {
                send(new IUI.SetTime(p_time));
            }
        } else {}
    }

    @Override
    public void deliver(Message message) {
        if (message != null) {
            switch (message.getId()) {
            case IUI.LAPRESETPRESSED:
                runMessageHandler(message, () -> lapResetPressed());
                break;
            case IUI.LIGHTPRESSED:
                runMessageHandler(message, () -> lightPressed());
                break;
            case IUI.MODEPRESSED:
                runMessageHandler(message, () -> modePressed());
                break;
            case IUI.STARTSTOPPRESSED:
                runMessageHandler(message, () -> startStopPressed());
                break;
            case IUI.NEWGOALSPEC:
                runMessageHandler(message, () -> newGoalSpec((GoalSpan) message.get("p_spanType"), (GoalCriteria) message.get("p_criteriaType"), (double) message.get("p_span"), (double) message.get("p_maximum"), (double) message.get("p_minimum"), (int) message.get("p_sequenceNumber")));
                break;
            case IUI.SETTARGETPRESSED:
                runMessageHandler(message, () -> setTargetPressed());
                break;
            default:
                throw new PortMessageException("Message not implemented by this port", getDomain(), this, message);
            }
        } else {
            throw new PortMessageException("Cannot deliver null message", getDomain(), this, message);
        }
    }

    @Override
    public Tracking getDomain() {
        return (Tracking) super.getDomain();
    }

}
