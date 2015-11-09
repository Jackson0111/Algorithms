/**
 * Created by oukouhou on 11/1/15.
 */


import java.util.*;


public class CarTracker {

    //global var here:
    HashMap<String, Integer> priceHashMap = new HashMap<>();
    HashMap<String, Integer> mileageHashMap = new HashMap<>();
    ArrayList<CarInfo> minPriceArray = new ArrayList<>();
    ArrayList<CarInfo> minMileageArray = new ArrayList<>();

//-------------------------------------------------------------------ADD A NEW CAR---------------------------------------------------------------//
    void addNewCar(){

        System.out.println("Please enter the following information split by comma:\n" +
                "A unique VIN number (17 character string of numbers and capital letters (but no I (i), O (o), or Q (q) to avoid confusion with numerals 1 and 0)\n" +
                "The car's make\n" +
                "The car's model\n" +
                "The price to purchase (in dollars)\n" +
                "The mileage of the car\n" +
                "And the color of the car\n");

        Scanner scan = new Scanner(System.in);

        String scanIn = scan.nextLine();

        String[] car = scanIn.split(",");

        String carVin = car[0];
        String carMake = car[1];
        String carModel = car[2];
        String carColor = car[3];
        int carPrice = Integer.parseInt(car[4]);
        int carMileage = Integer.parseInt(car[5]);

        CarInfo newCar = new CarInfo(carVin,carMake,carModel,carColor,carPrice,carMileage);

        minPriceArray.add(newCar);
        sortMinPrice(newCar);

        minMileageArray.add(newCar);
        sortMinMileage(newCar);

    }

    void sortMinPrice(CarInfo newCar){

        int childIndex = minPriceArray.size() - 1;

        while ( childIndex / 2 > 0 && (minPriceArray.get(childIndex / 2).price > minPriceArray.get(childIndex).price) ) {

            CarInfo temp = minPriceArray.get(childIndex / 2);

            minPriceArray.set(childIndex / 2, newCar);

            minPriceArray.set(childIndex, temp);

            //update the index in the hashmap
            priceHashMap.replace(minPriceArray.get(childIndex).vin, childIndex);

            childIndex /= 2;

        }

        priceHashMap.put(minPriceArray.get(childIndex).vin, childIndex);

    }


    void sortMinMileage(CarInfo newCar){

        int childIndex = minMileageArray.size() - 1;

        while ( childIndex / 2 > 0 && (minMileageArray.get(childIndex / 2).mileage > minMileageArray.get(childIndex).mileage) ) {

            CarInfo temp = minMileageArray.get(childIndex / 2);

            minMileageArray.set(childIndex / 2, newCar);

            minMileageArray.set(childIndex, temp);

            //update the index in the hashmap
            mileageHashMap.replace(minMileageArray.get(childIndex).vin, childIndex);

            childIndex /= 2;

        }

        mileageHashMap.put(minMileageArray.get(childIndex).vin, childIndex);

    }

//---------------------------------------------------------------------------------------------------------------------------------------------//


    //-------------------------------------------------------------------Update Function-----------------------------------------------------------//

    void update(){

        System.out.println("Please enter the VIN number for the car you wish to update.");

        Scanner scanner = new Scanner(System.in);

        String vinNum = scanner.nextLine();

        System.out.println("What would you like to update? Enter 1, 2, or 3 to update.\n" +
                "1 - The price of the car\n" +
                "2 - The mileage of the car\n" +
                "3 - The color of the car\n");

        String updateOption = scanner.nextLine();

        if (updateOption.equals( "1" )) {

            System.out.println("Please enter the new price without the dollar sign.");

            String enteredPrice = scanner.nextLine();
            int newPrice = Integer.parseInt(enteredPrice);

            update(vinNum, newPrice, "update price");

            System.out.println(priceHashMap.get(vinNum));

        }else if (updateOption.equals("2")){

            System.out.println("Please enter the new mileage without commas.");

            String enteredMileage = scanner.nextLine();
            int newMileage = Integer.parseInt(enteredMileage);

            update(vinNum, newMileage, "update mileage");

        }else {

            System.out.println("Please enter the new color.");

            String newColor = scanner.nextLine();

            minPriceArray.get(Integer.parseInt(vinNum)).color = newColor;

            minMileageArray.get(Integer.parseInt(vinNum)).color = newColor;

        }

    }

    void update(String vinNum, int newUpdate, String pq){

        // 1 - price needs to be updated in minPriceArray
        // 2 - minPriceArray needs to be sorted again
        // 3 - priceHashMap needs to be updated too
        if (pq.equals("update price")) {

            //PRICE UPDATE:

            int oldIndex = priceHashMap.get(vinNum);

            minPriceArray.get(oldIndex).price = newUpdate;

            if (minPriceArray.size() == 0) {

                System.out.println("No cars, can't update.");

            }

            int parent;

            int leftChild;

            int rightChild;

            while (oldIndex / 2 > 0 || 2 * oldIndex < minPriceArray.size() || 2 * oldIndex + 1 < minPriceArray.size()) {

                //It has a parent:
                if (oldIndex / 2 > 0) {

                    parent = minPriceArray.get(oldIndex / 2).price;

                    if (newUpdate < parent) {

                        //update the index of newPrice, then update the other indcies in moveUP()
                        priceHashMap.replace(vinNum, moveUp(minPriceArray.get(oldIndex), "update price"));

                        oldIndex /= 2;

                        continue;

                    }

                }

                //It has a left child
                if (2 * oldIndex < minPriceArray.size()) {

                    leftChild = minPriceArray.get(2 * oldIndex).price;

                    //It has a right child
                    if (2 * oldIndex + 1 < minPriceArray.size()) {

                        rightChild = minPriceArray.get(2 * oldIndex + 1).price;

                        //It's bigger than left Child
                        if (newUpdate > leftChild) {

                            //It is also bigger than right child
                            if (newUpdate > rightChild) {

                                //Then determine which child is smaller so we swap with that one:
                                if (leftChild <= rightChild) {

                                    //Swap with left child
                                    priceHashMap.replace(vinNum, moveDown(minPriceArray.get(oldIndex), minPriceArray.get(2 * oldIndex), "update price", "left"));

                                    oldIndex *= 2;

                                } else {

                                    //Swap with right child:
                                    priceHashMap.replace(vinNum, moveDown(minPriceArray.get(oldIndex), minPriceArray.get(2 * oldIndex + 1), "update price", "right"));

                                    oldIndex *= 2;
                                    oldIndex++;

                                }

                            } else {

                                //It is only bigger than the left child:
                                //Swap with left child:
                                priceHashMap.replace(vinNum, moveDown(minPriceArray.get(oldIndex), minPriceArray.get(2 * oldIndex), "update price", "left"));

                                oldIndex *= 2;

                            }

                        } else if (newUpdate > rightChild) {

                            //It is only bigger than right child:
                            //Swap with right child:
                            priceHashMap.replace(vinNum, moveDown(minPriceArray.get(oldIndex), minPriceArray.get(2 * oldIndex + 1), "update price", "right"));

                            oldIndex *= 2;
                            oldIndex++;

                        } else {
                            //It is not bigger than either of its children, we stop because it's at a proper position:

                            break;

                        }

                    } else {

                        //It doesn't have a right child:
                        //And It is bigger than left child:
                        //Swap with left child:
                        if (newUpdate > leftChild) {

                            priceHashMap.replace(vinNum, moveDown(minPriceArray.get(oldIndex), minPriceArray.get(2 * oldIndex), "update price", "left"));

                            oldIndex *= 2;

                        } else {

                            break;

                        }

                    }

                } else {
                    //It doesn't have any children:
                    //We stop:

                    break;

                }

            }
        }else {
            //MILEAGE UPDATE:

            int oldIndex = mileageHashMap.get(vinNum);

            minMileageArray.get(oldIndex).mileage = newUpdate;

            if (minMileageArray.size() == 0) {

                System.out.println("No cars, can't update.");

            }

            int parent;

            int leftChild;

            int rightChild;

            while (oldIndex / 2 > 0 || 2 * oldIndex < minMileageArray.size() || 2 * oldIndex + 1 < minMileageArray.size()) {

                //It has a parent:
                if (oldIndex / 2 > 0) {

                    parent = minMileageArray.get(oldIndex / 2).mileage;

                    if (newUpdate < parent) {

                        //update the index of newUpdate, then update the other indcies in moveUP()
                        mileageHashMap.replace(vinNum, moveUp(minMileageArray.get(oldIndex), "update mileage"));

                        oldIndex /= 2;

                        continue;

                    }

                }

                //It has a left child
                if (2 * oldIndex < minMileageArray.size()) {

                    leftChild = minMileageArray.get(2 * oldIndex).mileage;

                    //It has a right child
                    if (2 * oldIndex + 1 < minMileageArray.size()) {

                        rightChild = minMileageArray.get(2 * oldIndex + 1).mileage;

                        //It's bigger than left Child
                        if (newUpdate > leftChild) {

                            //It is also bigger than right child
                            if (newUpdate > rightChild) {

                                //Then determine which child is smaller so we swap with that one:
                                if (leftChild <= rightChild) {

                                    //Swap with left child
                                    mileageHashMap.replace(vinNum, moveDown(minMileageArray.get(oldIndex), minMileageArray.get(2 * oldIndex), "update mileage", "left"));

                                    oldIndex *= 2;

                                } else {

                                    //Swap with right child:
                                    mileageHashMap.replace(vinNum, moveDown(minMileageArray.get(oldIndex), minMileageArray.get(2 * oldIndex + 1), "update mileage", "right"));

                                    oldIndex *= 2;
                                    oldIndex++;

                                }

                            } else {

                                //It is only bigger than the left child:
                                //Swap with left child:
                                mileageHashMap.replace(vinNum, moveDown(minMileageArray.get(oldIndex), minMileageArray.get(2 * oldIndex), "update mileage", "left"));

                                oldIndex *= 2;

                            }

                        } else if (newUpdate > rightChild) {

                            //It is only bigger than right child:
                            //Swap with right child:
                            mileageHashMap.replace(vinNum, moveDown(minMileageArray.get(oldIndex), minMileageArray.get(2 * oldIndex + 1), "update mileage", "right"));

                            oldIndex *= 2;
                            oldIndex++;

                        } else {
                            //It is not bigger than either of its children, we stop because it's at a proper position:

                            break;

                        }

                    } else {

                        //It doesn't have a right child:
                        //And It is bigger than left child:
                        //Swap with left child:
                        if (newUpdate > leftChild) {

                            mileageHashMap.replace(vinNum, moveDown(minMileageArray.get(oldIndex), minMileageArray.get(2 * oldIndex), "update mileage", "left"));

                            oldIndex *= 2;

                        } else {

                            break;

                        }

                    }

                } else {
                    //It doesn't have any children:
                    //We stop:

                    break;

                }

            }
        }
    }


    int moveUp(CarInfo update, String pq){

        if (pq.equals("update price")){

            int index = priceHashMap.get(update.vin);

            int parentIndex = index / 2;

            CarInfo temp = minPriceArray.get(parentIndex);

            minPriceArray.set(parentIndex, minPriceArray.get(index));

            minPriceArray.set(index, temp);

            priceHashMap.replace(minPriceArray.get(index).vin, index);

            return parentIndex;

        }else {

            int index = mileageHashMap.get(update.vin);

            int parentIndex = index / 2;

            CarInfo temp = minMileageArray.get(parentIndex);

            minMileageArray.set(parentIndex, minMileageArray.get(index));

            minMileageArray.set(index, temp);

            mileageHashMap.replace(minMileageArray.get(index).vin, index);

            return parentIndex;

        }

    }

    int moveDown(CarInfo update, CarInfo child, String pq, String whichChild){

        if (pq.equals("update price")){

            //Swap with left child:
            if (whichChild.equals("left")){

                int index = priceHashMap.get(update.vin);

                int leftChildIndex = 2 * index;

                CarInfo temp = minPriceArray.get(leftChildIndex);

                minPriceArray.set(leftChildIndex, minPriceArray.get(index));

                minPriceArray.set(index, temp);

                priceHashMap.replace(minPriceArray.get(index).vin, index);

                return leftChildIndex;

            }else {
                //Swap with right child:
                int index = priceHashMap.get(update.vin);

                int rightChildIndex = 2 * index + 1;

                CarInfo temp = minPriceArray.get(rightChildIndex);

                minPriceArray.set(rightChildIndex, minPriceArray.get(index));

                minPriceArray.set(index, temp);

                priceHashMap.replace(minPriceArray.get(index).vin, index);

                return rightChildIndex;

            }

        }else {

            //Swap with left child:
            if (whichChild.equals("left")){

                int index = priceHashMap.get(update.vin);

                int leftChildIndex = 2 * index;

                CarInfo temp = minMileageArray.get(leftChildIndex);

                minMileageArray.set(leftChildIndex, minMileageArray.get(index));

                minMileageArray.set(index, temp);

                mileageHashMap.replace(minMileageArray.get(index).vin, index);

                return leftChildIndex;

            }else {
                //Swap with right child:
                int index = mileageHashMap.get(update.vin);

                int rightChildIndex = 2 * index + 1;

                CarInfo temp = minMileageArray.get(rightChildIndex);

                minMileageArray.set(rightChildIndex, minMileageArray.get(index));

                minMileageArray.set(index, temp);

                mileageHashMap.replace(minMileageArray.get(index).vin, index);

                return rightChildIndex;

            }

        }


    }
    //---------------------------------------------------------------------------------------------------------------------------------//



    //-------------------------------------------------Remove A Car------------------------------------------------------------------------//

    void remove(){

        System.out.println("What is the VIN number of the car you wish to remove?");

        Scanner scan = new Scanner(System.in);

        String vinNumber = scan.nextLine();

        int priceIndex = priceHashMap.get(vinNumber);

        int mileageIndex = mileageHashMap.get(vinNumber);

        // if the user wants to remove a leaf node or the size is 2, no need to call update
        if (2 * priceIndex >= minPriceArray.size() || 2 * priceIndex + 1 >= minPriceArray.size() || minPriceArray.size() == 2) {

            minPriceArray.remove(priceIndex);

        }else {

            //if the car is not at a leaf node, call update to remove
            CarInfo priceLeaf = minPriceArray.remove(minPriceArray.size() - 1);

            update(vinNumber, priceLeaf.price, "update price");

            String vin = priceLeaf.vin;

            String make = priceLeaf.make;

            String model = priceLeaf.model;

            int price = priceLeaf.price;

            int mileage = priceLeaf.mileage;

            String color = priceLeaf.color;

            priceIndex = priceHashMap.get(vinNumber);

            minPriceArray.get(priceIndex).vin = vin;

            minPriceArray.get(priceIndex).make = make;

            minPriceArray.get(priceIndex).model = model;

            minPriceArray.get(priceIndex).price = price;

            minPriceArray.get(priceIndex).mileage = mileage;

            minPriceArray.get(priceIndex).color = color;

            priceHashMap.remove(vinNumber);

            priceHashMap.put(vin, priceIndex);

        }

        if (2 * mileageIndex >= minMileageArray.size() || 2 * mileageIndex + 1 >= minMileageArray.size() || minMileageArray.size() == 2) {

            minMileageArray.remove(mileageIndex);

        }else {

            CarInfo mileageLeaf = minMileageArray.remove(minMileageArray.size() - 1);

            update(vinNumber, mileageLeaf.mileage, "update mileage");

            String vin = mileageLeaf.vin;

            String make = mileageLeaf.make;

            String model = mileageLeaf.model;

            int price = mileageLeaf.price;

            int mileage = mileageLeaf.mileage;

            String color = mileageLeaf.color;

            mileageIndex = mileageHashMap.get(vinNumber);

            minMileageArray.get(mileageIndex).vin = vin;

            minMileageArray.get(mileageIndex).make = make;

            minMileageArray.get(mileageIndex).model = model;

            minMileageArray.get(mileageIndex).price = price;

            minMileageArray.get(mileageIndex).mileage = mileage;

            minMileageArray.get(mileageIndex).color = color;

            mileageHashMap.remove(vinNumber);

            mileageHashMap.put(vin, mileageIndex);

        }


    }

//------------------------------------------------------------------------------------------------------------------------------------------------//

    //------------------------------------------------------Retrieve lowest price by make and model-----------------------------------------------//
    void lowestByMakeAndModel(String pq){

        System.out.println("What's the make?");

        Scanner scanner = new Scanner(System.in);

        String make = scanner.nextLine();

        System.out.println("What's the model?");

        String model = scanner.nextLine();

        if (pq.equals("price")) {

            for (int i = 1; i < minPriceArray.size(); i++) {

                if (minPriceArray.get(i).make.equals(make) && minPriceArray.get(i).model.equals(model)) {

                    System.out.println("The lowest price for a " + minPriceArray.get(i).make + " " + minPriceArray.get(i).model + " is: " + minPriceArray.get(i).price);

                    return;

                }

            }
            System.out.println("There is no such make and model in your collection.");
        }else {

            for (int i = 1; i < minMileageArray.size(); i++) {

                if (minMileageArray.get(i).make.equals(make) && minMileageArray.get(i).model.equals(model)) {

                    System.out.println("The lowest mileage for a " + minMileageArray.get(i).make + " " + minMileageArray.get(i).model + " is: " + minMileageArray.get(i).mileage);

                    return;

                }

            }

        }
    }



//-----------------------------------------------------------------------------------------------------------------------------------------------//



    //-------------------------------------------------------------------Car Info Class--------------------------------------------------------//
    public class CarInfo{

        int price, mileage;

        String vin, make, model, color;

        public CarInfo(String vin, String make, String model, String color, int price, int mileage){

            this.vin = vin;

            this.make = make;

            this.model = model;

            this.color = color;

            this.price = price;

            this.mileage = mileage;

        }

    }
//--------------------------------------------------------------------------------------------------------------------------------------------------//


    public static void main(String[] args){

        CarTracker carTracker = new CarTracker();

        // skip index at 0 to avoid confusion.
        carTracker.minPriceArray.add(null);
        carTracker.minMileageArray.add(null);

        System.out.println("What would you like to do?\n" +
                "Add a new car?\n" +
                "Update a car?\n" +
                "Remove a specific car?\n" +
                "Retrieve the lowest price car?\n" +
                "Retrieve the lowest mileage car?\n" +
                "Retrieve the lowest price car by make and mode?\n" +
                "Or retrieve the lowest mileage car by make and model?\n");

        Scanner userOption = new Scanner(System.in);

        while(true) {

            String userInput = userOption.nextLine().toLowerCase();

            if (userInput.equals("add a new car")) {

                carTracker.addNewCar();

                System.out.println("Your car has been added, what else do you want to do? Enter 'quit' if you wish to stop.");

            } else if (userInput.equals("update a car")) {

                carTracker.update();

                System.out.println("Your car has been updated, what else do you want to do? Enter 'quit' if you wish to stop.");

            } else if (userInput.equals("remove a specific car")) {

                carTracker.remove();

                System.out.println("Your car has been removed. what else do you want to do? Enter 'quit' if you wish to stop.");

            } else if (userInput.equals("retrieve the lowest price car")) {

                if (carTracker.minPriceArray.size() != 1) {

                    System.out.println("The lowest price car is a " + carTracker.minPriceArray.get(1).color + " " + carTracker.minPriceArray.get(1).make + " " +
                            carTracker.minPriceArray.get(1).model + " with " + carTracker.minPriceArray.get(1).mileage + " miles." + " Its price is " +
                            carTracker.minPriceArray.get(1).price + " dollars.\n");

                }else {

                    System.out.println("There are no cars in your collection");

                }
                System.out.println("What else do you want to do? Enter 'quit' if you wish to stop.");

            } else if (userInput.equals("retrieve the lowest mileage car")) {

                if (carTracker.minMileageArray.size() != 1) {

                    System.out.println("The lowest mileage car is a " + carTracker.minMileageArray.get(1).color + " " + carTracker.minMileageArray.get(1).make + " " +
                            carTracker.minMileageArray.get(1).model + " with " + carTracker.minMileageArray.get(1).mileage + " miles." + " Its price is " +
                            carTracker.minMileageArray.get(1).price + " dollars.\n");

                }else {

                    System.out.println("There are no cars in your collection");

                }
                System.out.println("What else do you want to do? Enter 'quit' if you wish to stop.");

            } else if (userInput.equals("retrieve the lowest price car by make and mode")) {

                carTracker.lowestByMakeAndModel("price");

                System.out.println("What else do you want to do? Enter 'quit' if you wish to stop.");

            } else if (userInput.equals("retrieve the lowest mileage car by make and model")) {

                carTracker.lowestByMakeAndModel("mileage");

                System.out.println("What else do you want to do? Enter 'quit' if you wish to stop.");

            }else if (userInput.equals("quit")){

                break;

            } else {

                System.out.println("Enter one of the options above, so i can stop this loop and get you what you want!");

            }
        }
    }

}
