//private boolean isAnswerCorrect(Map<String, ArrayList<String[]>> answerKey, Map<String, ArrayList<String[]>> userAnswers) {
//    for (String aminoAcid : answerKey.keySet()) {
//        ArrayList<String[]> keyCombos = answerKey.get(aminoAcid);
//        ArrayList<String[]> userCombos = userAnswers.get(aminoAcid);
//
//        // If the amino acid doesn't exist in user's answers or the sizes don't match, return false
//        if (userCombos == null || keyCombos.size() != userCombos.size()) {
//            return false;
//        }
//
//        // Convert to sets of sorted strings for comparison
//        Set<String> keySet = new HashSet<>();
//        Set<String> userSet = new HashSet<>();
//
//        for (String[] combo : keyCombos) {
//            keySet.add(createCanonicalForm(combo));
//        }
//        for (String[] combo : userCombos) {
//            userSet.add(createCanonicalForm(combo));
//        }
//
//        // Check if the sets are equal
//        if (!keySet.equals(userSet)) {
//            return false;
//        }
//    }
//
//    // If all amino acids and their combinations match, return true
//    return true;
//}
//
//private String createCanonicalForm(String[] combo) {
//    // Sort the array and join elements into a string to ensure consistency
//    Arrays.sort(combo);
//    return String.join(",", combo);
//}
//
