import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemParser {

    private int errors=0;

    public ArrayList<String> parseRawDataIntoStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawData);
        return response;
    }



    public Item parseStringIntoItem(String rawItem) throws ItemParseException {
        ArrayList<String> keyValuePairs;
        keyValuePairs = findKeyValuePairsInRawItemData(rawItem);

        List<String> values = keyValuePairs.stream().map(this::valueExtractor).collect(Collectors.toList());


        spellFixer(values);


        return new Item(values.get(0), !values.get(1).isEmpty()?Double.valueOf(values.get(1)):0, values.get(2), values.get(3));
    }

    private void spellFixer(List<String> values) {
        values.set(0, values.get(0).replaceAll("[0]","o"));
        values.set(2, values.get(2).replaceAll("[0]","o"));
    }

    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem) throws ItemParseException {
        String stringPattern = "[_+-,!@#$%^&*();\\|<>]";
        if(rawItem.contains (":;")){
            errors++;
            throw new ItemParseException();
        }
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawItem);
        if (response.size()!=4){
            throw new ItemParseException();
        }
        return response;
    }

    public String valueExtractor(String valuePair)  {
        String[] kv = valuePair.split(":");
        return kv.length==1 ? "" :kv[1].toLowerCase();
    }

    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString){
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }


    public int getErrors() {
        return errors;
    }


}
