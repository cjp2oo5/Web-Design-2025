 import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

 
class ComplaintManager {
    private ComplaintBST bst;  
    private MyLinkedList<Complaint> orderedList;  
    private ComplaintPriorityQueue pq;  

     
    public ComplaintManager() {
        this.bst = new ComplaintBST();
        this.orderedList = new MyLinkedList<>();
        this.pq = new ComplaintPriorityQueue();
    }

    
    public synchronized void submitComplaint(Complaint c) throws ValidationException {
        if (c == null) throw new ValidationException("Complaint is null");
        if (c.getTitle() == null || c.getTitle().trim().isEmpty()) throw new ValidationException("Missing title");
        
        boolean inserted = bst.insert(c);
        if (!inserted) throw new ValidationException("Duplicate complaint ID");
        
        // Add to all three data structures
        orderedList.addLast(c);
        pq.add(c);
    }

    
    public Complaint findById(String id) {
        return bst.searchById(id);
    }

    
    public boolean updateStatus(String id, Complaint.Status status) {
        Complaint c = bst.searchById(id);
        if (c == null) return false;
        c.setStatus(status);
        return true;
    }

    /**
     * Deletes a complaint from the BST.
     */
    public boolean deleteById(String id) {
        // Note: This only removes from BST.
        // A complete implementation would also remove from LinkedList and PQ.
        return bst.delete(id);
    }

    /**
     * Returns the complete, chronologically-ordered list of complaints.
     */
    public MyLinkedList<Complaint> listChronological() {
        return orderedList;
    }

    /**
     * Returns an array of all complaints, sorted by priority
     * using a simple Insertion Sort.
     */
    public Complaint[] sortedByPriority() {
        int n = orderedList.size();
        Complaint[] arr = new Complaint[n];
        int i = 0;
        for (Complaint c: orderedList) arr[i++] = c;
        
        // Simple stable insertion sort by priority
        for (int j=1;j<n;j++){
            Complaint key = arr[j];
            int k = j-1;
            while (k >=0 && arr[k].getPriority() > key.getPriority()){
                arr[k+1] = arr[k]; k--;
            }
            arr[k+1] = key;
        }
        return arr;
    }

    /**
     * Gets the next most urgent complaint from the PQ without removing it.
     * @return The highest-priority Complaint, or null if empty.
     */
    public Complaint peekNextComplaint() {
        return pq.peek();
    }

    /**
     * Processes and removes the next most urgent complaint from the PQ.
     * It also updates its status to "UNDER_INVESTIGATION".
     * @return The processed Complaint, or null if empty.
     */
    public Complaint processNextComplaint() {
        Complaint c = pq.poll(); // Remove from priority queue
        if (c != null) {
            c.setStatus(Complaint.Status.UNDER_INVESTIGATION);
        }
        return c;
    }

    /**
     * Returns the number of unprocessed complaints in the priority queue.
     * @return The size of the priority queue.
     */
    public int getPendingQueueSize() {
        return pq.size();
    }
}



/**
 * Custom Singly-Linked List implementation.
 * Used for storing complaints in chronological order.
 */
class MyLinkedList<T> implements Iterable<T> {
    private MyLinkedListNode<T> head;
    private int size = 0;

    public MyLinkedList() { head = null; }

    /** Adds a new item to the end of the list. O(n) */
    public void addLast(T value) {
        MyLinkedListNode<T> n = new MyLinkedListNode<>(value);
        if (head == null) { head = n; size++; return; }
        MyLinkedListNode<T> cur = head;
        while (cur.next != null) cur = cur.next;
        cur.next = n;
        size++;
    }

    /** Removes the first occurrence of a value. O(n) */
    public boolean remove(T value) {
        MyLinkedListNode<T> cur = head, prev = null;
        while (cur != null) {
            if ((cur.data == null && value == null) || (cur.data != null && cur.data.equals(value))) {
                if (prev == null) head = cur.next; else prev.next = cur.next;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    public int size() { return size; }

    /** Allows the list to be used in a for-each loop. */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private MyLinkedListNode<T> cur = head;
            @Override
            public boolean hasNext() { return cur != null; }
            @Override
            public T next() {
                if (cur == null) throw new NoSuchElementException();
                T d = cur.data; cur = cur.next; return d;
            }
        };
    }
}

/**
 * Node class for the MyLinkedList.
 */
class MyLinkedListNode<T> {
    public T data;
    public MyLinkedListNode<T> next;
    public MyLinkedListNode(T data) { this.data = data; this.next = null; }
}


/**
 * Custom Binary Search Tree implementation.
 * Used for fast, O(log n) lookup of complaints by ID.
 */
class ComplaintBST {
    private BSTNode root;
    private int size = 0;

    public ComplaintBST() { root = null; }

    /**
     * Inserts a new Complaint into the BST based on its ID.
     * @param c The Complaint to insert.
     * @return true if successful, false if duplicate ID.
     */
    public boolean insert(Complaint c) {
        if (root == null) { root = new BSTNode(c); size++; return true; }
        BSTNode cur = root, parent = null;
        while (cur != null) {
            int cmp = c.getComplaintId().compareTo(cur.value.getComplaintId());
            if (cmp == 0) return false; // duplicate id
            parent = cur;
            if (cmp < 0) cur = cur.left; else cur = cur.right;
        }
        if (c.getComplaintId().compareTo(parent.value.getComplaintId()) < 0) parent.left = new BSTNode(c);
        else parent.right = new BSTNode(c);
        size++;
        return true;
    }

    /**
     * Searches for a Complaint by its ID. O(log n) on average.
     * @param id The ID to search for.
     * @return The Complaint, or null if not found.
     */
    public Complaint searchById(String id) {
        BSTNode cur = root;
        while (cur != null) {
            int cmp = id.compareTo(cur.value.getComplaintId());
            if (cmp == 0) return cur.value;
            if (cmp < 0) cur = cur.left; else cur = cur.right;
        }
        return null;
    }

    /** Standard BST deletion logic. */
    public boolean delete(String id) {
        BSTNode[] result = deleteRec(root, id);
        if (result == null) return false;
        root = result[0];
        boolean deleted = result[1] != null;
        if (deleted) size--;
        return deleted;
    }

    /** Recursive helper for deletion. */
    private BSTNode[] deleteRec(BSTNode node, String id) {
        if (node == null) return new BSTNode[]{null, null};
        int cmp = id.compareTo(node.value.getComplaintId());
        if (cmp < 0) {
            BSTNode[] res = deleteRec(node.left, id);
            node.left = res[0];
            return new BSTNode[]{node, res[1]};
        } else if (cmp > 0) {
            BSTNode[] res = deleteRec(node.right, id);
            node.right = res[0];
            return new BSTNode[]{node, res[1]};
        } else {
            if (node.left == null) return new BSTNode[]{node.right, node};
            if (node.right == null) return new BSTNode[]{node.left, node};
            BSTNode successorParent = node;
            BSTNode successor = node.right;
            while (successor.left != null) { successorParent = successor; successor = successor.left; }
            node.value = successor.value;
            if (successorParent.left == successor) successorParent.left = successor.right;
            else successorParent.right = successor.right;
            return new BSTNode[]{node, successor};
        }
    }

    public int size() { return size; }
    
    // In-order traversal (for testing/logging)
    public List<Complaint> inOrder() {
        List<Complaint> list = new ArrayList<>();
        inOrderRec(root, list);
        return list;
    }
    private void inOrderRec(BSTNode node, List<Complaint> list) {
        if (node == null) return;
        inOrderRec(node.left, list);
        list.add(node.value);
        inOrderRec(node.right, list);
    }
}

/**
 * Node class for the ComplaintBST.
 */
class BSTNode {
    public Complaint value;
    public BSTNode left, right;
    public BSTNode(Complaint v) { value = v; left = right = null; }
}

/**
 * Custom Priority Queue implementation using a Min-Heap.
  */
class ComplaintPriorityQueue {
    private ArrayList<Complaint> heap;

    public ComplaintPriorityQueue() {
        heap = new ArrayList<>();
    }

    public int size() { return heap.size(); }
    public boolean isEmpty() { return heap.isEmpty(); }

    /**
     * Adds a new complaint to the queue. O(log n).
      */
    public void add(Complaint c) {
        heap.add(c);
        siftUp(heap.size() - 1); // Maintain heap property
    }

    /**
     * Retrieves but does not remove the highest-priority complaint. O(1).
      */
    public Complaint peek() {
        if (heap.isEmpty()) return null;
        return heap.get(0);
    }

    /**
     * Retrieves AND removes the highest-priority complaint. O(log n).
     * @return The Complaint, or null if empty.
     */
    public Complaint poll() {
        if (heap.isEmpty()) return null;
        Complaint root = heap.get(0);
        Complaint last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0); // Maintain heap property
        }
        return root;
    }

    /** Helper to move a new element up the heap. */
    private void siftUp(int index) {
        if (index == 0) return;
        int parentIndex = (index - 1) / 2;
        if (compare(heap.get(index), heap.get(parentIndex)) < 0) {
            swap(index, parentIndex);
            siftUp(parentIndex);
        }
    }

    /** Helper to move a new root element down the heap. */
    private void siftDown(int index) {
        int leftChildIndex = 2 * index + 1;
        int rightChildIndex = 2 * index + 2;
        int smallest = index;

        if (leftChildIndex < heap.size() && compare(heap.get(leftChildIndex), heap.get(smallest)) < 0) {
            smallest = leftChildIndex;
        }
        if (rightChildIndex < heap.size() && compare(heap.get(rightChildIndex), heap.get(smallest)) < 0) {
            smallest = rightChildIndex;
        }
        if (smallest != index) {
            swap(index, smallest);
            siftDown(smallest);
        }
    }

    /** Helper to swap two elements in the heap. */
    private void swap(int i, int j) {
        Complaint temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /** Compares two complaints by their priority number. */
    private int compare(Complaint c1, Complaint c2) {
        return Integer.compare(c1.getPriority(), c2.getPriority());
    }
}


// ====================================================================
// 3. DATA MODELS AND UTILITIES
// ====================================================================

/**
 * The main data model for a Complaint.
 * Implements Comparable to be used in the BST.
 */
class Complaint implements Comparable<Complaint> {
    public enum Status { RECEIVED, UNDER_INVESTIGATION, RESOLVED, REJECTED }

    private final String complaintId;
    private final String title;
    private final String description;
    private final String reporterId;
    private final LocalDateTime createdAt;
    private int priority;
    private Status status;

    public Complaint(String complaintId, String title, String description, String reporterId, int priority) {
        this.complaintId = complaintId;
        this.title = title;
        this.description = description;
        this.reporterId = reporterId;
        this.createdAt = LocalDateTime.now();
        this.priority = priority;
        this.status = Status.RECEIVED;
    }

    // Getters and Setters
    public String getComplaintId() { return complaintId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getReporterId() { return reporterId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("ID:%s | %s | by:%s | %s | priority:%d | %s",
                complaintId, title, reporterId, createdAt.toString(), priority, status);
    }

    /**
     * Compares this Complaint to another by ID.
     * This is required for the Binary Search Tree.
     */
    @Override
    public int compareTo(Complaint other) {
        return this.complaintId.compareTo(other.complaintId);
    }
}

/**
 * Abstract base class for all users.
 */
abstract class User {
    private String userId;
    private String name;
    private String email;

    protected User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public abstract String getRole();
}

/**
 * Represents a person submitting a complaint.
 */
class Complainant extends User {
    public Complainant(String userId, String name, String email) {
        super(userId, name, email);
    }

    @Override
    public String getRole() { return "Complainant"; }
}

/**
 * Represents an officer processing complaints.
 */
class Officer extends User {
    private String department;

    public Officer(String userId, String name, String email, String department) {
        super(userId, name, email);
        this.department = department;
    }

    @Override
    public String getRole() { return "Officer"; }
    public String getDepartment() { return department; }
}

/**
 * Utility for generating unique, thread-safe IDs.
 */
class IDGenerator {
    private static AtomicInteger compCounter = new AtomicInteger(1000);
    public static String nextComplaintId() {
        return "C" + compCounter.incrementAndGet(); // Use incrementAndGet
    }
    
    private static AtomicInteger userCounter = new AtomicInteger(2000);
    public static String nextUserId() {
        return "U" + userCounter.incrementAndGet();
    }
}

/**
 * Custom exception for handling invalid data submission.
 */
class ValidationException extends Exception {
    public ValidationException(String msg) { super(msg); }
}