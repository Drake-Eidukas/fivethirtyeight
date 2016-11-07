package io.eidukas.fivethirtyeight;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.eidukas.fivethirtyeight.Views.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_settings), withContentDescription("Settings"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.plus_button_id), withText("Polls-Plus Model"),
                        withParent(withId(R.id.model_select_id)),
                        isDisplayed()));
        appCompatRadioButton.perform(click());

        ViewInteraction appCompatRadioButton2 = onView(
                allOf(withId(R.id.alphabetic_button_id), withText("Sort list by state alphabetically"),
                        withParent(withId(R.id.sort_select_id)),
                        isDisplayed()));
        appCompatRadioButton2.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Apply"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.state_list_item), withText("Alabama"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.state_list_view),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Alabama")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.state_list_item), withText("Alaska"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.state_list_view),
                                        1),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Alaska")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.state_list_item), withText("Arkansas"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.state_list_view),
                                        3),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Arkansas")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.state_list_item), withText("Delaware"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.state_list_view),
                                        7),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Delaware")));

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.action_refresh), withContentDescription("Refresh"), isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.action_settings), withContentDescription("Settings"), isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction appCompatRadioButton3 = onView(
                allOf(withId(R.id.probability_button_id), withText("Sort list by probability to win"),
                        withParent(withId(R.id.sort_select_id)),
                        isDisplayed()));
        appCompatRadioButton3.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Apply"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.probability_list_item), withText("100.00%"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.state_list_view),
                                        0),
                                2),
                        isDisplayed()));
        textView5.check(matches(withText("100.00%")));

        ViewInteraction actionMenuItemView4 = onView(
                allOf(withId(R.id.action_settings), withContentDescription("Settings"), isDisplayed()));
        actionMenuItemView4.perform(click());

        ViewInteraction appCompatRadioButton4 = onView(
                allOf(withId(R.id.candidate_button_id), withText("Sort list by candidate"),
                        withParent(withId(R.id.sort_select_id)),
                        isDisplayed()));
        appCompatRadioButton4.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Apply"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.candidate_list_item), withText("Clinton"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.state_list_view),
                                        0),
                                1),
                        isDisplayed()));
        textView6.check(matches(withText("Clinton")));

        ViewInteraction actionMenuItemView5 = onView(
                allOf(withId(R.id.action_settings), withContentDescription("Settings"), isDisplayed()));
        actionMenuItemView5.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button2), withText("Cancel"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton4.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
