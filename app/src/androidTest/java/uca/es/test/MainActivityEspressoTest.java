package uca.es.test;

import android.app.Activity;
import android.content.Intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class,false,true);

    @Test
    public void Autenticar(){
        onView(withId(R.id.eUsuario))
                .perform(typeText("633907618"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.ePass))
                .perform(typeText("hola"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.button)).perform(click());

        //Comprobar que hemos nos hemos autenticado con éxito
        //onView(withId(R.id.pacientes)).check(matches(withText("PACIENTES")));
    }



}
