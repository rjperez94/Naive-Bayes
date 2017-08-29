# Naive-Bayes

## Compiling Java files using Eclipse IDE

1. Download this repository as ZIP
2. Create new `Java Project` in `Eclipse`
3. Right click on your `Java Project` --> `Import`
4. Choose `General` --> `Archive File`
5. Put directory where you downloaded ZIP in `From archive file`
6. Put `ProjectName/src` in `Into folder`
7. Click `Finish`

## Running the program

1. Right click on your `Java Project` --> `Run As` --> `Java Application`
2. Program will ask for directory of where the text files are
3. Choose `data` directory
4. Program will look for `spamLabelled.dat`, and `spamUnlabelled.dat` inside that chosen folder

## About the Data File

The data `data/spamLabelled.dat` describes 200 emails, labelled as `spam` or `non-spam`. Each email is specified by 12 binary attributes, indicating the presence of features such as "MILLION DOLLARS", significant amounts of text in CAPS, an invalid reply-to address, and so on. Note that there are 2<sup>12</sup>=4096 possible input patterns, compared to a data set of just 200 examples

The layout of the data is that each row is an instance of features from one email, and columns correspond to the features, which are binary: the feature is either there or not. The last column is the class: `1 = spam` and `0 = non-spam` 

The file `data/spamUnlabelled.dat` contains 10 new input patterns to be classified
