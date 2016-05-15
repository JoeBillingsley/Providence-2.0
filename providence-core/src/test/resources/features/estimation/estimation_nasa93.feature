Feature: Estimating a deadline
  The system must be able to produce a deadline for a particular project

  Scenario:
    Given a syntactically valid .arff file is at './src/test/resources/arff/nasa93.arff'
    And the actual effort is contained in the feature 'act_effort'
    And the probability of a crossover step occurring is 0.9, the probability of a mutation step occurring is 0.03
    And the evolution is run for 200 generations and the population have an anneal time of 50 generations
    And the population size is 100
    When the outliers are removed
    And the model is trained using 80% of the data, and tested using Monte Carlo cross validation with 10 splits
    Then the mean average LSD is less than 1.2418
    And the mean average MMRE is less than 1.8031
    And the mean average PRED25 is greater than 0.1848