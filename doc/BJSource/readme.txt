http://www.geditcom.com/Blackjack/Blackjack.html

The Blackjack executable is meant to be run in a command-line environment. Each run does a selected set of calculations and outputs the results to the command-line window or to a selected file. The format is 

Blackjack [calculations] [options]

where [calculations] specify the desired calculations to be run and the [options] select various options for running those calculations. 

Calculations
The follow calculations can be run. They can be done individually or in combination:

-S 
Find expected values for standing on all possible two-card initial hands. 
-H 
Find expected values for hitting all possible two-card initial hands and then continuing to play by basic strategy. 
-D 
Find expected values for doubling down on all possible two-card initial hands. 
-A 
Find the expected values for splitting all possible pairs of cards. For each pair the table will include results for splitting and resplitting. For each of them, the table will have results for doubling down after splitting not allowed, allowed on any two cards, or allowed only on hands of 10 or 11. Each line will have six results. The calculations when resplitting is not allowed use methods developed by Peter Griffin [1]. The calculations when resplitting is allowed use the new and highly accurate approximate methods developed by the author [2]. 
-En 
Find the expected values for splitting all possible pairs of cards with resplitting allowed up to a making of n hands (where n is the number immediately after the E). This calculation uses the methods developed by the author that make them possible [2], but still require a lengthy calculation. For each pair the table will include three results for doubling down after splitting not allowed, allowed on any two cards, or allowed only on hands of 10 or 11. For interactive splitting calculations, you should use the approximate approach in the -A option. For exact results using the -En option you can refer to tabulated results on this web site or be prepared to dedicate are large amount of computer time. 
-R2 
This option provides an alternate method for exact splitting calculations. It is less efficient that the -En option and should never be used. It is the "Recursive" method described in a paper by the author [2]. 
-C 
This option considers each player hand against the current dealer up card, finds expected values for standing, hitting, doubling down, and splitting (when allowed) and outputs the optimum choice (i.e., the highest expected value) and which option was the optimum choice (i.e., S, H, D, or P for stand, hit, double, or split). For doubling down and splitting, the expected value will be calculated only for the current rules. Use the -l or -r option to set the doubling down rule and the -n and -m options to set the splitting rules for the output table. The splitting calculations use the approximate methods (see -A option). 
-G 
Peter Griffin's book [1] describes a set of blackjack tables that considers expected value for each strategy decision (e.g., hitting an abstract total of 14 vs. dealer up card 7). For each decision it calculates the difference between that decision and the opposite decision (e.g., hitting vs. standing) calling that the "mean." It then repeats the calculation for decks in which a single card of one denomination is removed and finds the change in mean value for each type of removed card calling that the "effect of card removal." The full table thus has 11 calculations - one for the mean value and one for effect of removing each card (1 through 9 and T). For doubling down and splitting, the table will be calculated only for the current rules. Use the -l or -r option to set the doubling down rule and the -n and -m options to set the splitting rules for the output table. The splitting calculations use the approximate methods (see -A option).

This command calculates the mean value with a full deck (i.e., the player cards and the dealer up card are not removed from the deck). This approach is identical to the one taken by Griffin [1] although the results are not identical to Griffin's tables. The reason for the discrepancy is not known, but these results are believed to be accurate. The output table will list means and effects in percents. The table will include only strategy decisions for which the mean value is greater than -25%; those with lower means are unlikely to be relevant in counting calculations. See Griffin's book for more details [1]. 
-g 
This command calculates the same Griffin tables as the -G option except that it starts with a deck in which the dealer up card has been removed. The approach was mentioned by Griffin [1] and might provide tables for slightly more accurate counting calculations. 
Options
-o fname
By default the results are sent to standard output. Use this option to divert the results to a tab-delimited plain-text file. Give the output filename in fname. 
-d2 
Set the number of decks. The number must follow the d without a space and must be in the range 1 to 8. The default is 1 deck. 
-s or -h 
Set the dealer play. The -s options sets the dealer to stand on soft 17; the -h option sets the dealer to hit soft 17. The default is dealer stand on soft 17. 
-i4 -f8 
The output table will include expected values for dealer up cards from the initial or -i option to the final or -f option. The card number must follow the i or f without a space and be 1 to 9 or T for ten. The sequence can be increasing or decreasing depending on whether the final value is higher or lower than the initial value. The default settings are initial card 1 (or ace) and final card T. 
-l or -r 
Set the doubling down rules. This rule is only needed when calculating Combination (option -C) or Griffin (options -G and -g) tables. The settings are -l for doubling down on any two cards (Las Vegas rules) or -r for doubling down on 10 & 11 only (Reno rules). The default is -l. 
-n 
Set the doubling down after splitting rule. This rule is only needed when calculating Combination (option -C) or Griffin (options -G and -g) tables. By default doubling down after splitting is allowed. Use this -n option to prohibit it. When doubling down after splitting is allowed, it will use the current doubling down rule set with the -l or -r commands. 
-m 
Set the resplitting rule. This rule is only needed when calculating Combination (option -C) or Griffin (options -G and -g) tables. By default resplitting is not allowed. Use this -m option to allow resplitting. 
-c num 
A dealer cache helps the calculations run much faster. The details are described in a paper by the author [2]. The num is the size of the cache and is limited to 0 through 23. A size of 15 is sufficient for most calculations. Exact splitting calculations can benefit from a larger cache. The maximum size of 23 requires 2.23 G or memory for the cache; if you have less memory an error will result and you will need to reduce the cache size. 
-v 
When used, the calculations will provide verbose feedback to standard output. This output will only appear with the -o option is used to divert calculations to a file. 
-? 
Print a list of all command options with brief descriptions. 
Sample Calculations
This section lists some sample calculations. The first few can generate all the results in the posted tables. 

Blackjack -SHDA -d1 -c 18 -o bjtable.txt 
This command generates a complete table for a single deck game when the dealer stands on soft 17. The results would be identical to the posted single deck results except would only include the approximate splitting results. 
Blackjack -SHDA -i1f6 -h -d1 -c 18 -o bjtable.txt 
This command generates a complete table for a single deck game when the dealer hits 17. The results would be identical to the posted single deck results except would only include the approximate splitting results. The results for dealer up card 7 to ten are identical for dealer stands or hits soft 17. Thus if the results when dealer stands on soft 17 are complete, the results for dealer hits soft 17 need only do up cards ace (or 1) through 6) 
Blackjack -E2 -d1 -c 23 -o bjtable.txt 
This command generates a table of exact expected values for pair splitting when resplitting is not allowed in a single deck game when the dealer stands on soft 17. The results would be identical to the exact splitting sections of the single deck results. Warning, this command would take a long time run. 
Blackjack -E4 -d1 -c 23 -o bjtable.txt 
This command generates a table of exact expected values for pair splitting when resplitting is allowed (to a maximum of four hands) in a single deck game when the dealer stands on soft 17. The results would be identical to the exact splitting sections of the single deck results. Warning, this command would take about 45 days to run on a machine with a 3 GHz processor. A better approach is to submit jobs on separate processors and use the -i and -f options to set each job to get results for a single up card. 
Blackjack -C -d4 -l -m -c 18 -o bjtable.txt 
This command calculates combination tables for all dealer up cards in a four deck game with doubling down on any two cards, doubling down after splitting allowed, and resplitting allowed. 
Blackjack -G -d1 -r -n -c 18 -o bjtable.txt 
This command calculates Griffin tables [1] for all dealer up cards in a single deck game with doubling down on 10 & 11 only, no doubling down after splitting allowed, and resplitting not allowed. 
