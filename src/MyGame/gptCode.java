//package MyGame;//private boolean isAnswerCorrect(Map<String, ArrayList<String[]>> answerKey, Map<String, ArrayList<String[]>> userAnswers) {
////    for (String aminoAcid : answerKey.keySet()) {
////        ArrayList<String[]> keyCombos = answerKey.get(aminoAcid);
////        ArrayList<String[]> userCombos = userAnswers.get(aminoAcid);
////
////        // If the amino acid doesn't exist in user's answers or the sizes don't match, return false
////        if (userCombos == null || keyCombos.size() != userCombos.size()) {
////            return false;
////        }
////
////        // Convert to sets of sorted strings for comparison
////        Set<String> keySet = new HashSet<>();
////        Set<String> userSet = new HashSet<>();
////
////        for (String[] combo : keyCombos) {
////            keySet.add(createCanonicalForm(combo));
////        }
////        for (String[] combo : userCombos) {
////            userSet.add(createCanonicalForm(combo));
////        }
////
////        // Check if the sets are equal
////        if (!keySet.equals(userSet)) {
////            return false;
////        }
////    }
////
////    // If all amino acids and their combinations match, return true
////    return true;
////}
////
////private String createCanonicalForm(String[] combo) {
////    // Sort the array and join elements into a string to ensure consistency
////    Arrays.sort(combo);
////    return String.join(",", combo);
////}
////
//
//import java.util.*;
//
//public class gptCode
//{
//
//    public boolean checkResponses(final Map<String, String[]> answerKey, final Map<String, String[]> userResponses)
//    {
//        // Flatten and normalize answer key
//        Map<String, List<Set<String>>> normalizedAnswerKey;
//        Map<String, List<Set<String>>> normalizedUserResponses;
//
//        normalizedAnswerKey     = flattenBonds(answerKey);
//        normalizedUserResponses = flattenBonds(userResponses);
//
//        // Compare the keys
//        if(!normalizedAnswerKey.keySet().equals(normalizedUserResponses.keySet()))
//        {
//            return false;
//        }
//
//        // Compare the bond lists for each key
//        for(final String key : normalizedAnswerKey.keySet())
//        {
//            List<Set<String>> expectedBonds;
//            List<Set<String>> userBonds;
//
//            expectedBonds = normalizedAnswerKey.get(key);
//            userBonds     = normalizedUserResponses.get(key);
//
//            if(expectedBonds.size() != userBonds.size() || !userBonds.containsAll(expectedBonds))
//            {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Takes the answerKey and userResponses and FLATTENS it for easier comparison:
//     * <p>
//     * Flattens this Map type:  Map<String, String[][]> ---to---> Map<String, List<String>>
//     *
//     * @param bonds
//     *
//     * @return
//     */
//    public Map<String, List<Set<String>>> flattenBonds(final Map<String, String[][]> bonds)
//    {
//
//        Map<String, List<Set<String>>> flattened;
//        flattened = new HashMap<>();
//
//        for(final Map.Entry<String, String[][]> entry : bonds.entrySet())
//        {
//            String            key;
//            String[][]        bondPairs;
//            List<Set<String>> bondListPlaceholder;
//
//            key                 = entry.getKey();
//            bondPairs           = entry.getValue();
//            bondListPlaceholder = new ArrayList<>();
//
//            // Iterate through all Map Values and flatten to List<String>
//            for(final String[] pair : bondPairs)
//            {
//                // Add each pair to the placeholder list
//                bondListPlaceholder.add(new HashSet<>(Arrays.asList(pair)));
//            }
//
//            flattened.put(key, bondListPlaceholder);
//        }
//        return flattened;
//    }
//}
