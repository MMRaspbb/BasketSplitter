package com.ocado.basket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

public class BasketSplitter {
    private final Map<String, List<String>> products;

    public BasketSplitter(String absolutePathToConfigFile){ // reading json file and creating HashMap variable out of it
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(absolutePathToConfigFile);
            Type type = new TypeToken<HashMap<String, List<String>>>() {}.getType();
            this.products = gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Wrong absolute path to file");
        }
    }

    public Map<String, List<String>> split(List<String> items) {
        //creating a list of possible combinations of suppliers that can deliver all the products
        //combinations are made with class BitMask which is a representation of binary number with boolean array

        List<String> suppliersList = countAllPossibleSuppliers(items);
        int availableSuppliersNumber = suppliersList.size();

        ArrayList<boolean[]> suppliersGroupCandidates = new ArrayList<>();

        boolean foundCoveringSuppliers = false;
        for (int i = 0; i < availableSuppliersNumber; i++) {
            BitMask bitMask = new BitMask(availableSuppliersNumber);
            for (int j = 0; j < Math.pow(2, availableSuppliersNumber) - 1; j++) {
                bitMask.increment();
                if (bitMask.sumBinaryNumber() == i + 1 && suppliersCoverProducts(suppliersList, bitMask.getNumber(), items)) {
                    foundCoveringSuppliers = true;
                    suppliersGroupCandidates.add(Arrays.copyOf(bitMask.getNumber(), availableSuppliersNumber));
                }
            }
            if (foundCoveringSuppliers) {
                break;
            }
        }
        return chooseBestCombination(items, suppliersGroupCandidates, suppliersList);
    }

    public List<String> countAllPossibleSuppliers(List<String> items) {
        //method creates a list of suppliers that can deliver at least 1 product from the basket
        HashSet<String> suppliers = new HashSet<>();
        for (String item : items) {
            suppliers.addAll(products.get(item));
        }
        return new ArrayList<>(suppliers);
    }

    private boolean suppliersCoverProducts(List<String> suppliersList, boolean[] combination, List<String> items) {
        //method checks if suppliers dictated by combination can deliver all the products
        boolean[] covered = new boolean[items.size()];
        for (int i = 0; i < combination.length; i++) {
            if (combination[i]) {
                for (int j = 0; j < items.size(); j++) {
                    if (products.get(items.get(j)).contains(suppliersList.get(i))) {
                        covered[j] = true;
                    }
                }
            }
        }
        for(boolean bit: covered){
            if(!bit){
                return false;
            }
        }
        return true;
    }

    private HashMap<String, List<String>> chooseBestCombination(List<String> items, List<boolean[]> combinations, List<String> suppliersList) {
        //choosing combination from the list of possible combination that covers the products in a way where the biggest group can take the maximum amount of products
        int maxProductForOneSupplier = 0;
        boolean[] bestCombination = new boolean[suppliersList.size()];
        String bestSupplier = "";
        for (boolean[] combination : combinations) {
            int maxLocalProducts = 0;
            int bestLocalSupplierIndex = 0;
            for (int j = 0; j < combination.length; j++) {
                if (combination[j]) {
                    int currentSupplierSum = 0;
                    for (String item : items) {
                        if (products.get(item).contains(suppliersList.get(j))) {
                            currentSupplierSum += 1;
                        }
                    }
                    if (currentSupplierSum > maxLocalProducts) {
                        maxLocalProducts = currentSupplierSum;
                        bestLocalSupplierIndex = j;
                    }
                }
            }
            if (maxLocalProducts > maxProductForOneSupplier) {
                maxProductForOneSupplier = maxLocalProducts;
                bestCombination = combination;
                bestSupplier = suppliersList.get(bestLocalSupplierIndex);
            }
        }
        //creating map variable and assigning products to the suppliers
        HashMap<String, List<String>> map = new HashMap<>();
        for (int i = 0; i < bestCombination.length; i++) {
            if (bestCombination[i]) {
                map.put(suppliersList.get(i), new ArrayList<>());
            }
        }
        for (String item : items) {
            if (products.get(item).contains(bestSupplier)) {
                map.get(bestSupplier).add(item);
            } else {
                for (String key : map.keySet()) {
                    if (!key.equals(bestSupplier) && products.get(item).contains(key)) {
                        map.get(key).add(item);
                        break;
                    }
                }
            }
        }
        return map;
    }
}
