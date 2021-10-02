package uca.es.test;

import android.view.MenuItem;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class PacienteActivityEspressoTest {

    @Rule
    public ActivityTestRule<PacienteActivity> mActivityRule =
            new ActivityTestRule<>(PacienteActivity.class,false,true);

    @Test
    public void Pruebas_Paciente() {

        onView(withId(R.id.pacientes))
                .perform(click());

        onView(withId(R.id.bAdd))
                .perform(click());

        onView(withId(R.id.enombre)).perform(replaceText("Miguel Ángel"));

        onView(withId(R.id.eapellido)).perform(replaceText("Zara García"));
        onView(withId(R.id.efnac)).perform(replaceText("15/01/1996"));
        onView(withId(R.id.etelefono)).perform(replaceText("633907620"));
        onView(withId(R.id.epass)).perform(replaceText("contracontra"));

        onView(withId(R.id.addPaciente))
                .perform(click());

        RecyclerView rv = mActivityRule.getActivity().findViewById(R.id.my_recycler_view);


        //actualizamos la recyclerView pa
        onView(withId(R.id.recarga))
                .perform(click());

        //Comprobar que hemos añadido el paciente correctamente
        onView((withText("Miguel Ángel"))).check(matches(isDisplayed()));


        //onView(withId(R.id.show)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        RecyclerView recyclerView = mActivityRule.getActivity().findViewById(R.id.my_recycler_view);
        int itemCount = recyclerView.getAdapter().getItemCount();

        //CONSULTA PACIENTE

        onView(withId(R.id.my_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(itemCount-1, MyViewAction.clickChildViewWithId(R.id.show)));

        //MODIFICA PACIENTE

        onView(withId(R.id.editar))
                .perform(click());

        onView(withId(R.id.enombre)).perform(replaceText("Miguel Editado"));

        onView(withId(R.id.editar))
                .perform(click());

        //Comprobamos que se ha editado el nombre de forma correcta
        onView((withId(R.id.tNombre_paciente))).check(matches(withText("Miguel Editado")));

        //ELIMINAR PACIENTE

        onView(withId(R.id.eliminar))
                .perform(click());

        onView(withText("SI"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

    }

    public void rellenar_cuestionario_sintomas() throws InterruptedException {
        onView(withId(R.id.pacientes))
                .perform(click());

        onView(withId(R.id.my_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.show)));

        onView(withId(R.id.banamnesis))
                .perform(click());

        Thread.sleep(2000);

        //Entramos en la pregunta ¿Orina de Noche?

        onView(withId(R.id.my_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, MyViewAction.clickChildViewWithId(R.id.show)));

        Thread.sleep(2000);

        //Respondemos NO
        onView(withId(R.id.my_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, MyViewAction.clickChildViewWithId(R.id.radio)));

        //Comprobamos que la respuesta está introducida

        onView(withId(R.id.recarga)).perform(click());

        onView((withText("Respuesta: NO"))).check(matches(isDisplayed()));

    }


}
