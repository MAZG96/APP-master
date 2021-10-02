package uca.es.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CitaActivityEspressoTest {

    @Rule
    public ActivityTestRule<CitasActivity> mActivityRule =
            new ActivityTestRule<>(CitasActivity.class,false,true);

    @Test
    public void Pruebas_Citas() {

        //CREAR CITA
        onView(withId(R.id.bAdd))
                .perform(click());

        //Elegimos el paciente al que asignar la cita
        onView(withId(R.id.my_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.radio)));

        //Elegimos la fecha y hora de la cita
        onView(withId(R.id.bbuscar))
                .perform(click());

        //Elegimos la fecha en el calendario
        onView(withText("Aceptar"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        //Elegimos la hora
        onView(withId(R.id.my_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.radio)));

        //Confirmamos la cita
        onView(withId(R.id.baceptar))
                .perform(click());

        //MODIFICAR CITA

        onView(withId(R.id.my_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.show)));

        onView(withId(R.id.bModificar))
                .perform(click());

        //Elegimos la fecha y hora de la cita
        onView(withId(R.id.bbuscar))
                .perform(click());

        //Elegimos la fecha en el calendario
        onView(withText("Aceptar"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        //Cambiamos la hora de la cita
        onView(withId(R.id.my_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, MyViewAction.clickChildViewWithId(R.id.radio)));

        //Confirmamos la cita
        onView(withId(R.id.baceptar))
                .perform(click());
        
    }


}
