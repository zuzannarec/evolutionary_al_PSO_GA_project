package jmetal.metaheuristics.singleObjective.particleSwarmOptimization;

import jmetal.core.Algorithm;
import jmetal.core.Algorithm2;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.singleObjective.Rastrigin;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Hybrid_main {
    public static Logger logger_ ;      // Logger object
    public static FileHandler fileHandler_ ; // FileHandler object

    /**
     * @param args Command line arguments. The first (optional) argument specifies
     *             the problem to solve.
     * @throws JMException
     * @throws IOException
     * @throws SecurityException
     * Usage: three options
     *      - jmetal.metaheuristics.mocell.MOCell_main
     *      - jmetal.metaheuristics.mocell.MOCell_main problemName
     *      - jmetal.metaheuristics.mocell.MOCell_main problemName ParetoFrontFile
     */
    public static void main(String [] args)
            throws JMException, IOException, ClassNotFoundException {
        Problem problemPSO   ;  // The problem to solve
        Algorithm2 algorithmPSO ;  // The algorithm to use
        Mutation mutationPSO  ;  // "Turbulence" operator

        Problem   problemGA   ;         // The problem to solve
        Algorithm algorithmGA ;         // The algorithm to use
        Operator crossoverGA ;         // Crossover operator
        Operator  mutationGA  ;         // Mutation operator
        Operator  selectionGA ;         // Selection operator

        QualityIndicator indicators ; // Object to get quality indicators

        HashMap parametersPSO ; // Operator parameters
        HashMap  parametersGA ; // Operator parameters

        // Logger object and file to store log messages
        logger_      = Configuration.logger_ ;
        fileHandler_ = new FileHandler("PSO_main.log");
        logger_.addHandler(fileHandler_) ;

        problemPSO = new Rastrigin("Real", 20);

        algorithmPSO = new PSO(problemPSO) ;

        algorithmGA = new gGA(problemPSO) ; // Generational GA


        // Algorithm parameters GA
        algorithmGA.setInputParameter("populationSize",500);
        algorithmGA.setInputParameter("maxEvaluations", 500);

        // Mutation and Crossover for Real codification
        parametersGA = new HashMap() ;
        parametersGA.put("probability", 0.9) ;
        parametersGA.put("distributionIndex", 20.0) ;
        crossoverGA = CrossoverFactory.getCrossoverOperator("SBXCrossover", parametersGA);

        parametersGA = new HashMap() ;
        parametersGA.put("probability", 1.0/problemPSO.getNumberOfVariables()) ;
        parametersGA.put("distributionIndex", 20.0) ;
        mutationGA = MutationFactory.getMutationOperator("PolynomialMutation", parametersGA);

        // Selection Operator GA
        parametersGA = null ;
        selectionGA = SelectionFactory.getSelectionOperator("BinaryTournament", parametersGA) ;

        // Add the operators to the algorithm GA
        algorithmGA.addOperator("crossover",crossoverGA);
        algorithmGA.addOperator("mutation",mutationGA);
        algorithmGA.addOperator("selection",selectionGA);


        // Algorithm parameters PSO
        algorithmPSO.setInputParameter("swarmSize",500);
        algorithmPSO.setInputParameter("maxIterations",500);

        parametersPSO = new HashMap() ;
        parametersPSO.put("probability", 1.0/problemPSO.getNumberOfVariables()) ;
        parametersPSO.put("distributionIndex", 20.0) ;
        mutationPSO = MutationFactory.getMutationOperator("PolynomialMutation", parametersPSO);

        algorithmPSO.addOperator("mutation", mutationPSO);



        // Execute the Algorithm
        long initTime = System.currentTimeMillis();
        SolutionSet populationGA = algorithmGA.execute();
        SolutionSet populationPSO = algorithmPSO.execute(populationGA);
        long estimatedTime = System.currentTimeMillis() - initTime;

        // Result messages
        //logger_.info("Total execution time: "+estimatedTime + "ms");
        //logger_.info("Objectives values have been writen to file FUN");
        //populationPSO.printObjectivesToFile("FUN");
        //logger_.info("Variables values have been writen to file VAR");
        //populationPSO.printVariablesToFile("VAR");
    } //main
} // PSO_main
