package fr.tse.pri.parsers.easytimeline;

/* Generated By:JavaCC: Do not edit this line. EasyTimeLineConstants.java */

/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface EasyTimeLineConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int PERIOD = 3;
  /** RegularExpression Id. */
  int DATEFORMAT = 4;
  /** RegularExpression Id. */
  int FROM = 5;
  /** RegularExpression Id. */
  int AT = 6;
  /** RegularExpression Id. */
  int FROM_START = 9;
  /** RegularExpression Id. */
  int TILL_START = 10;
  /** RegularExpression Id. */
  int YEAR_START = 11;
  /** RegularExpression Id. */
  int DATE_START = 12;
  /** RegularExpression Id. */
  int FORMAT_DATEFORMAT = 15;
  /** RegularExpression Id. */
  int TILL_DATA_FROM = 19;
  /** RegularExpression Id. */
  int YEAR_DATA_FROM = 20;
  /** RegularExpression Id. */
  int DATE_DATA_FROM = 21;
  /** RegularExpression Id. */
  int SPECIAL_DATA_FROM = 22;
  /** RegularExpression Id. */
  int YEAR_DATA_AT = 26;
  /** RegularExpression Id. */
  int DATE_DATA_AT = 27;
  /** RegularExpression Id. */
  int SPECIAL_DATA_AT = 28;
  /** RegularExpression Id. */
  int TEXT_DATA_AT_TOTEXT = 32;
  /** RegularExpression Id. */
  int YEAR_DATA_TILL = 36;
  /** RegularExpression Id. */
  int DATE_DATA_TILL = 37;
  /** RegularExpression Id. */
  int SPECIAL_DATA_TILL = 38;
  /** RegularExpression Id. */
  int TEXT_DATA_TILL_TOTEXT = 42;
  /** RegularExpression Id. */
  int WORDS_DATA_TEXT_IN = 51;
  /** RegularExpression Id. */
  int UNEXPECTED_CHAR = 52;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int TIMELINE_START = 1;
  /** Lexical state. */
  int TIMELINE_DATEFORMAT = 2;
  /** Lexical state. */
  int TIMELINE_DATA_FROM = 3;
  /** Lexical state. */
  int TIMELINE_DATA_AT = 4;
  /** Lexical state. */
  int TIMELINE_DATA_AT_TOTEXT = 5;
  /** Lexical state. */
  int TIMELINE_DATA_TILL = 6;
  /** Lexical state. */
  int TIMELINE_DATA_TILL_TOTEXT = 7;
  /** Lexical state. */
  int TIMELINE_DATA_TEXT = 8;
  /** Lexical state. */
  int TIMELINE_DATA_TEXT_IN = 9;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "<token of kind 2>",
    "<PERIOD>",
    "<DATEFORMAT>",
    "<FROM>",
    "<AT>",
    "<token of kind 7>",
    "\" \"",
    "\"from:\"",
    "\"till:\"",
    "<YEAR_START>",
    "<DATE_START>",
    "<token of kind 13>",
    "\" \"",
    "<FORMAT_DATEFORMAT>",
    "<token of kind 16>",
    "\" \"",
    "<token of kind 18>",
    "\"till:\"",
    "<YEAR_DATA_FROM>",
    "<DATE_DATA_FROM>",
    "<SPECIAL_DATA_FROM>",
    "<token of kind 23>",
    "\" \"",
    "<token of kind 25>",
    "<YEAR_DATA_AT>",
    "<DATE_DATA_AT>",
    "<SPECIAL_DATA_AT>",
    "<token of kind 29>",
    "\" \"",
    "<token of kind 31>",
    "\"text:\"",
    "<token of kind 33>",
    "\" \"",
    "<token of kind 35>",
    "<YEAR_DATA_TILL>",
    "<DATE_DATA_TILL>",
    "<SPECIAL_DATA_TILL>",
    "<token of kind 39>",
    "\" \"",
    "<token of kind 41>",
    "\"text:\"",
    "<token of kind 43>",
    "\"[[\"",
    "\" \"",
    "<token of kind 46>",
    "<token of kind 47>",
    "\"]]\"",
    "\" \"",
    "<token of kind 50>",
    "<WORDS_DATA_TEXT_IN>",
    "<UNEXPECTED_CHAR>",
  };

}
