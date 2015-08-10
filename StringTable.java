import java.util.Arrays;
//
// STRINGTABLE.JAVA
// A hash table mapping Strings to their positions in the the pattern sequence
//
public class StringTable {
		Record[] table;
		int maxSize;
		int numItems;
		int h;
		Record d;

	//
	// Create an empty table big enough to hold maxSize records.
	//
	public StringTable(int maxSize) 
	{
		//Instantiate table at 2, ignore maxSize argument (for Part II)
		this.numItems = 0;
		this.maxSize = 2;
		this.d= new Record("deleted");
		this.table = new Record[this.maxSize];
		this.h = 1;
	}
	//
	// Insert a Record r into the table.  Return true if
	// successful, false if the table is full.  
	public boolean insert(Record r) 
	{ 
		String key = r.key;
		int keyValue = toHashKey(key);
		int slotI = baseHash(keyValue);
		int i = 0;

		if(table[slotI] == null || table[slotI].compareHashVal(d)){
			numItems++;
			table[slotI] = r;
			//if alpha is greater than .25, double
			if((maxSize < 4*numItems) ){ 
				doubleTheTable();
			}
			return true;
		}
		while ( (i < maxSize ) && !( (table[slotI] == null) || ( table[slotI].compareHashVal(d) ) ) ){
			slotI = ( (slotI + stepHash(keyValue) ) % maxSize);
			i++;
		}
		if(table[slotI] == null || table[slotI].compareHashVal(d)){
			table[slotI] = r;
			numItems++;
			//if alpha is greater than .25, double table size
			if((maxSize < 4*numItems) ){
				doubleTheTable();
			}
			return true;
		}
		return false; 
	}


	//
	// Delete a Record r from the table. 
	//
	public void remove(Record r) 
	{
		String key = r.key;
		int keyValue = toHashKey(key);
		int slotI = baseHash(keyValue);
		
		if(table[slotI] == null){
			return;
		}
		if(table[slotI].compareHashVal(r)){
			table[slotI] = d;	
			numItems--;
			return;
		}
		int i = 0;
		while ( (i < maxSize ) && !( (table[slotI] == null) || (table[slotI].compareHashVal(r)) ) ) {
			slotI = ( ( slotI + stepHash(keyValue) ) % maxSize);
			i++;
		}
		if (table[slotI] == null) {
			return;
		}
		if (table[slotI].compareHashVal(r)){
			table[slotI] = d;
			numItems--;
			return;
		}
	}


	//
	// Find a record with a key matching the input.  Return the
	// record if it exists, or null if no matching record is found.
	//
	public Record find(String key) 
	{
		int keyValue = toHashKey(key);
		int slotI = baseHash(keyValue);
		Record z = new Record(key);

		if(table[slotI] == null){
			return null;
		}
		if (table[slotI].compareHashVal(z)){
			return table[slotI];
		}
		int i = 0;
		while ( (i < maxSize ) && !( (table[slotI] == null) || (table[slotI].compareHashVal(z) ) ) ){
			slotI = ( ( slotI + stepHash(keyValue) ) % maxSize);
			i++;
		}
		if(table[slotI] == null){
			return null;
		}
		if(table[slotI].compareHashVal(z)){
			return table[slotI];
		}
		return null;
	}

	public void doubleTheTable(){
		Record[] oldTable = Arrays.copyOf(table, this.maxSize );
		
		//make new size (double the old one)
		h++;
		this.maxSize = (int) Math.pow(2, h);
		this.table = new Record[this.maxSize];

		//For every slot in old table with Record, call insert method to put it in new table
		for(int k=0; k< oldTable.length; k++){
			if(oldTable[k] !=null && !oldTable[k].compareHashVal(d)){
				this.insert(oldTable[k]);
			}
		}
	}

	int toHashKey(String s)
	{
		int A = 1952786893;
		int B = 367257;
		int v = B;

		for (int j = 0; j < s.length(); j++)
		{
			char c = s.charAt(j);
			v = A * (v + (int) c + j) + B;
		}

		if (v < 0) v = -v;
		return v;
	}

	int baseHash(int hashKey)
	{
		//hash function 
		return (int) Math.floor( maxSize * ( ( ( (Math.sqrt(2) ) /2 ) * hashKey) - (Math.floor(hashKey*( (Math.sqrt(2) ) /2 ) ) ) ) );
	}

	int stepHash(int hashKey)
	{
		//hash function
		int s = (int) Math.floor(maxSize * ( ( ( (Math.sqrt(5)-1) /2 ) * hashKey) - (Math.floor(hashKey*( (Math.sqrt(5)-1) /2) ) ) ) );
		//Must make sure it is odd
		if((s % 2)==0){
			return s+1;
		}else{
			return s;
		}
	}


}
