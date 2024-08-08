package org.cloud.sonic.driver.ios.models;

import java.time.Duration;
import org.cloud.sonic.driver.common.tool.StringTool;
import org.cloud.sonic.driver.ios.enums.ActionType;
import org.cloud.sonic.driver.ios.models.TouchActions.TouchAction;
import org.cloud.sonic.driver.ios.models.TouchActions.TouchAction.Options;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;

public class W3CActions {
    public static Sequence convert(final TouchActions touchActions) {
    	final PointerInput FINGER = new PointerInput(Kind.TOUCH, "finger-"+StringTool.generateShortUuid());
    	final Sequence seq = new Sequence(FINGER, 0 /* https://github.com/appium/appium/issues/11273#issuecomment-416636734 */);
    	for (TouchAction action: touchActions.getActions()) {
    		final String strAction = action.getAction();
    		final Options actionData = action.getOptions();
    		if (strAction.equals(ActionType.MOVE.getType())) {
    			final Integer optMs = actionData.getMs();
    			final long ms = (optMs==null ? 0 : Math.max(optMs, 0));
    			seq.addAction(FINGER.createPointerMove(Duration.ofMillis(ms), Origin.viewport(), actionData.getX(), actionData.getY()));
    		} else if (strAction.equals(ActionType.PRESS.getType())) {
    			final Integer optMs = actionData.getMs();
    			final long ms = (optMs==null ? 0 : Math.max(optMs, 0));
    			seq.addAction(FINGER.createPointerMove(Duration.ofMillis(ms), Origin.viewport(), actionData.getX(), actionData.getY()));
    			seq.addAction(FINGER.createPointerDown(MouseButton.LEFT.asArg()));
    		} else if (strAction.equals(ActionType.RELEASE.getType())) {
    			seq.addAction(FINGER.createPointerUp(MouseButton.LEFT.asArg()));
    		} else if (strAction.equals(ActionType.WAIT.getType())) {
    			final Integer optMs = actionData.getMs();
    			final long ms = (optMs==null ? 0 : Math.max(optMs, 0));
    			if (ms<=0) { continue; } // end if
    			seq.addAction(new Pause(FINGER, Duration.ofMillis(ms)));
    		} // end if
    	} // end for
    	return seq;
    } // end convert()
} // end class

/*
References:
https://stackoverflow.com/a/71038411/12857692
https://github.com/appium/appium-espresso-driver/issues/244
https://github.com/appium/appium/issues/11273
*/
