package net.thucydides.cucumber.web;

import com.github.goldin.spock.extensions.tempdir.TempDir;
import net.thucydides.core.model.TestOutcome
import net.thucydides.core.model.TestResult;
import net.thucydides.core.model.TestStep;
import net.thucydides.core.reports.OutcomeFormat;
import net.thucydides.core.reports.TestOutcomeLoader;
import net.thucydides.core.util.MockEnvironmentVariables
import net.thucydides.cucumber.integration.PassingWebTestSampleWithNestedSteps
import net.thucydides.cucumber.integration.SimpleSeleniumFailingAndPassingScenario;
import net.thucydides.cucumber.integration.SimpleSeleniumScenario
import net.thucydides.cucumber.integration.SimpleSeleniumSeveralScenarios;
import org.junit.Test;
import spock.lang.Specification;

import java.io.File;
import java.util.List;

import static net.thucydides.cucumber.util.CucumberRunner.thucydidesRunnerForCucumberTestRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class WhenRunningCucumberStoriesWithScreenshots extends Specification {

    @TempDir
    File outputDirectory

    def environmentVariables = new MockEnvironmentVariables()

    def setup() {
        environmentVariables.setProperty("webdriver.driver", "phantomjs");
    }


    def "web tests should take screenshots"() {
        given:
        def runtime = thucydidesRunnerForCucumberTestRunner(SimpleSeleniumFailingAndPassingScenario, outputDirectory, environmentVariables);

        when:
        runtime.run();
        def recordedTestOutcomes = new TestOutcomeLoader().forFormat(OutcomeFormat.JSON).loadFrom(outputDirectory);

        then:
        TestStep given = givenStepIn(recordedTestOutcomes);
        given.getScreenshots().size() > 0
    }

    def "web tests should take screenshots with multiple scenarios"() {

        given:
        def runtime = thucydidesRunnerForCucumberTestRunner(SimpleSeleniumSeveralScenarios, outputDirectory, environmentVariables);

        when:
        runtime.run();
        def recordedTestOutcomes = new TestOutcomeLoader().forFormat(OutcomeFormat.JSON).loadFrom(outputDirectory);

        then:
        TestStep given = givenStepIn(recordedTestOutcomes);
        given.getScreenshots().size() > 0
    }

    def "web tests should take screenshots for multiple tests"()  {

        given:
        def runtime = thucydidesRunnerForCucumberTestRunner(SimpleSeleniumSeveralScenarios, outputDirectory, environmentVariables);

        when:
        runtime.run();
        def recordedTestOutcomes = new TestOutcomeLoader().forFormat(OutcomeFormat.JSON).loadFrom(outputDirectory);

        then:
        TestStep given1 = givenStepIn(recordedTestOutcomes,0);
        TestStep given2 = givenStepIn(recordedTestOutcomes,1);

        given1.getScreenshots().size() > 0
        given2.getScreenshots().size() > 0
    }


    def "web tests should take screenshots with nested step libraries"()  {

        given:
        def runtime = thucydidesRunnerForCucumberTestRunner(PassingWebTestSampleWithNestedSteps, outputDirectory, environmentVariables);

        when:
        runtime.run();
        def recordedTestOutcomes = new TestOutcomeLoader().forFormat(OutcomeFormat.JSON).loadFrom(outputDirectory);

        then:
        TestStep given = givenStepIn(recordedTestOutcomes,0);

        given.getScreenshots().size() > 0

    }

    protected TestStep givenStepIn(List<TestOutcome> outcomes) {
        return givenStepIn(outcomes,0);
    }

    protected TestStep givenStepIn(List<TestOutcome> outcomes, int index) {
        TestStep givenStep = outcomes.get(index).getTestSteps().get(0);
        if (!givenStep.getDescription().startsWith("Given")) {
            givenStep = givenStep.getChildren().get(0);
        }
        return givenStep;
    }

   /*

    @Test
    public void web_tests_should_take_screenshots_for_multiple_tests() throws Throwable {

        // Given
        ThucydidesJUnitStories story = newStory("*PassingBehaviorWithSeleniumAndSeveralScenarios.story");

        // When
        run(story);

        // Then
        List<TestOutcome> outcomes = loadTestOutcomes();

        TestStep given1 = givenStepIn(outcomes,0);
        TestStep given2 = givenStepIn(outcomes,1);
        TestStep given3 = givenStepIn(outcomes,2);
        TestStep given4 = givenStepIn(outcomes,3);

        assertThat(given1.getScreenshots().size(), greaterThan(0));
        assertThat(given2.getScreenshots().size(), greaterThan(0));
        assertThat(given3.getScreenshots().size(), greaterThan(0));
        assertThat(given4.getScreenshots().size(), greaterThan(0));
    }                                                */

    //@Test
    //public void web_tests_should_take_screenshots_with_nested_step_libraries() throws Throwable {

        // Given
     //   ThucydidesJUnitStories story = newStory("**/aPassingWebTestSampleWithNestedSteps.story");
     //   story.setEnvironmentVariables(environmentVariables);

        // When
       // run(story);

        // Then
       // List<TestOutcome> outcomes = loadTestOutcomes();
       // TestStep given = givenStepIn(outcomes);
       // assertThat(given.getScreenshots().size(), greaterThan(0));

    //}
}
