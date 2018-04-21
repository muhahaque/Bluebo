package edu.swarthmore.cs.cs71.blueboapplication;

import android.support.design.widget.BottomNavigationView;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class MainActivityTests {
    //TODO update to reflect change to bottom bar (Notifications --> Marketplace)

    /* Create rule to be able to launch MainActivity during tests

     */
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity = null;

    /* Set active Activity to MainActivity before each test

     */
    @Before
    public void setUp() {
        mainActivity = mainActivityActivityTestRule.getActivity();
    }

    /* Make sure that our bottom bar navigation successfully launches automatically

     */
    @Test
    public void bottomBarLaunches() {
        BottomNavigationView nav = mainActivity.findViewById(R.id.navigation);
        assertNotNull(nav);
    }

    /* Make sure that the Transactions page in bottom bar navigation is brought up on launch of MainActivity.

     */
    @Test
    public void transactionsFragmentLaunchesAtStartup() {
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("transactions_fragment");
        assertEquals(fragment.getTag(), "transactions_fragment");
    }

    /*  Make sure that user can navigate to Transactions using bottom bar.

     */
    @Test
    public void transactionsFragmentLaunchesOnBottomNavClick() {
        onView(withId(R.id.navigation_trade)).perform(click());
        onView(withId(R.id.navigation_transactions)).perform(click());
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("transactions_fragment");
        assertEquals(fragment.getTag(), "transactions_fragment");
    }

    /* Make sure that user can navigate to Trade page using bottom bar.

     */
    @Test
    public void tradeFragmentLaunchesOnBottomNavClick() {
        onView(withId(R.id.navigation_trade)).perform(click());
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("trade_fragment");
        assertEquals(fragment.getTag(), "trade_fragment");
    }

    /* Make sure that user can navigate to Notifications page using bottom bar.

     */
    @Test
    public void notificationsFragmentLaunchesOnBottomNavClick() {
        onView(withId(R.id.navigation_notifications)).perform(click());
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("notifications_fragment");
        assertEquals(fragment.getTag(), "notifications_fragment");
    }

    /* Set active Activity to null after each test to have a clean slate for next test

     */
    @After
    public void tearDown() {
        mainActivity = null;
    }
}
