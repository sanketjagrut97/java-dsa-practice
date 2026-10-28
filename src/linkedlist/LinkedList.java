public class LinkedList {

// ==================== NODE CLASS ====================

private static class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}

// ==================== INSTANCE VARIABLES ====================

private Node head;
private Node tail;
private int size;

public LinkedList() {
    head = null;
    tail = null;
    size = 0;
}

// ==================== BASIC OPERATIONS ====================

public int size() {
    return size;
}

public boolean isEmpty() {
    return size == 0;
}

public void print() {
    Node current = head;

    while (current != null) {
        System.out.print(current.data + " -> ");
        current = current.next;
    }

    System.out.println("null");
}

// ==================== INSERTION ====================

public void addFirst(int data) {
    Node newNode = new Node(data);

    if (isEmpty()) {
        head = tail = newNode;
    } else {
        newNode.next = head;
        head = newNode;
    }

    size++;
}

public void addLast(int data) {
    Node newNode = new Node(data);

    if (isEmpty()) {
        head = tail = newNode;
    } else {
        tail.next = newNode;
        tail = newNode;
    }

    size++;
}

public void add(int index, int data) {
    checkPositionIndex(index);

    if (index == 0) {
        addFirst(data);
        return;
    }

    if (index == size) {
        addLast(data);
        return;
    }

    Node previous = getNode(index - 1);
    Node newNode = new Node(data);

    newNode.next = previous.next;
    previous.next = newNode;

    size++;
}

// ==================== DELETION ====================

public int removeFirst() {
    ensureNotEmpty();

    int removedData = head.data;

    if (size == 1) {
        head = tail = null;
    } else {
        head = head.next;
    }

    size--;
    return removedData;
}

public int removeLast() {
    ensureNotEmpty();

    int removedData = tail.data;

    if (size == 1) {
        head = tail = null;
    } else {
        Node previous = getNode(size - 2);
        previous.next = null;
        tail = previous;
    }

    size--;
    return removedData;
}

public int remove(int index) {
    checkElementIndex(index);

    if (index == 0) {
        return removeFirst();
    }

    if (index == size - 1) {
        return removeLast();
    }

    Node previous = getNode(index - 1);
    Node target = previous.next;

    previous.next = target.next;

    size--;
    return target.data;
}

// ==================== SEARCHING ====================

public int search(int target) {
    Node current = head;
    int index = 0;

    while (current != null) {
        if (current.data == target) {
            return index;
        }

        current = current.next;
        index++;
    }

    return -1;
}

public int recursiveSearch(int target) {
    return recursiveSearch(head, target, 0);
}

private int recursiveSearch(Node current, int target, int index) {
    if (current == null) {
        return -1;
    }

    if (current.data == target) {
        return index;
    }

    return recursiveSearch(current.next, target, index + 1);
}

// ==================== REVERSAL ====================

public void reverse() {
    Node previous = null;
    Node current = head;

    tail = head;

    while (current != null) {
        Node next = current.next;

        current.next = previous;

        previous = current;
        current = next;
    }

    head = previous;
}

// ==================== REMOVE NTH NODE FROM END ====================

public int removeNthFromEnd(int n) {
    if (n <= 0 || n > size) {
        throw new IndexOutOfBoundsException(
                "n must be between 1 and the list size"
        );
    }

    Node dummy = new Node(0);
    dummy.next = head;

    Node fast = dummy;
    Node slow = dummy;

    for (int i = 0; i < n; i++) {
        fast = fast.next;
    }

    while (fast.next != null) {
        fast = fast.next;
        slow = slow.next;
    }

    Node target = slow.next;
    slow.next = target.next;

    if (target == tail) {
        tail = slow == dummy ? null : slow;
    }

    head = dummy.next;
    size--;

    return target.data;
}

// ==================== FIND MIDDLE ====================

// For an even-length list, returns the second middle node.
private Node findMiddleNode() {
    Node slow = head;
    Node fast = head;

    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }

    return slow;
}

public int findMiddle() {
    ensureNotEmpty();
    return findMiddleNode().data;
}

// ==================== PALINDROME CHECK ====================

public boolean isPalindrome() {
    if (size <= 1) {
        return true;
    }

    Node firstHalfEnd = head;
    Node fast = head;

    // Find the last node of the first half.
    while (fast.next != null && fast.next.next != null) {
        firstHalfEnd = firstHalfEnd.next;
        fast = fast.next.next;
    }

    // Reverse the second half.
    Node secondHalfStart = reverseFrom(firstHalfEnd.next);
    firstHalfEnd.next = secondHalfStart;

    boolean palindrome = true;

    Node left = head;
    Node right = secondHalfStart;

    while (right != null) {
        if (left.data != right.data) {
            palindrome = false;
            break;
        }

        left = left.next;
        right = right.next;
    }

    // Restore the original list.
    firstHalfEnd.next = reverseFrom(secondHalfStart);

    return palindrome;
}

private Node reverseFrom(Node start) {
    Node previous = null;
    Node current = start;

    while (current != null) {
        Node next = current.next;
        current.next = previous;
        previous = current;
        current = next;
    }

    return previous;
}

// ==================== CYCLE DETECTION ====================

public boolean isCycle() {
    Node slow = head;
    Node fast = head;

    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;

        if (slow == fast) {
            return true;
        }
    }

    return false;
}

// Removes a cycle, if one exists.
public void removeCycle() {
    Node slow = head;
    Node fast = head;
    boolean cycleFound = false;

    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;

        if (slow == fast) {
            cycleFound = true;
            break;
        }
    }

    if (!cycleFound) {
        return;
    }

    slow = head;

    // If the cycle starts at the head, find its last node.
    if (slow == fast) {
        while (fast.next != slow) {
            fast = fast.next;
        }

        fast.next = null;
    } else {
        // Find the node whose next link closes the cycle.
        while (slow.next != fast.next) {
            slow = slow.next;
            fast = fast.next;
        }

        fast.next = null;
    }

    // Recalculate tail and size after restoring an ordinary list.
    tail = head;
    size = 0;

    Node current = head;

    while (current != null) {
        tail = current;
        size++;
        current = current.next;
    }
}

// ==================== MERGE SORT ====================

public void mergeSort() {
    head = mergeSortNodes(head);

    tail = head;

    if (tail != null) {
        while (tail.next != null) {
            tail = tail.next;
        }
    }
}

private Node mergeSortNodes(Node start) {
    if (start == null || start.next == null) {
        return start;
    }

    Node middle = getMergeSortMiddle(start);
    Node rightHead = middle.next;

    // Split the list into two independent halves.
    middle.next = null;

    Node left = mergeSortNodes(start);
    Node right = mergeSortNodes(rightHead);

    return merge(left, right);
}

private Node getMergeSortMiddle(Node start) {
    Node slow = start;
    Node fast = start.next;

    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }

    return slow;
}

private Node merge(Node left, Node right) {
    Node dummy = new Node(0);
    Node current = dummy;

    while (left != null && right != null) {
        if (left.data <= right.data) {
            current.next = left;
            left = left.next;
        } else {
            current.next = right;
            right = right.next;
        }

        current = current.next;
    }

    current.next = (left != null) ? left : right;

    return dummy.next;
}

// ==================== ZIGZAG REORDERING ====================

public void zigzag() {
    if (size <= 2) {
        return;
    }

    Node firstHalfEnd = head;
    Node fast = head;

    // Find the last node of the first half.
    while (fast.next != null && fast.next.next != null) {
        firstHalfEnd = firstHalfEnd.next;
        fast = fast.next.next;
    }

    // Split the list into two halves.
    Node secondHalf = firstHalfEnd.next;
    firstHalfEnd.next = null;

    // Reverse the second half.
    secondHalf = reverseFrom(secondHalf);

    // Interleave nodes from both halves.
    Node first = head;
    Node second = secondHalf;

    while (second != null) {
        Node firstNext = first.next;
        Node secondNext = second.next;

        first.next = second;
        second.next = firstNext;

        first = firstNext;
        second = secondNext;
    }

    // Recalculate tail.
    tail = head;

    while (tail.next != null) {
        tail = tail.next;
    }
}

// ==================== INTERNAL HELPERS ====================

private Node getNode(int index) {
    Node current = head;

    for (int i = 0; i < index; i++) {
        current = current.next;
    }

    return current;
}

private void checkPositionIndex(int index) {
    if (index < 0 || index > size) {
        throw new IndexOutOfBoundsException(
                "Index: " + index + ", Size: " + size
        );
    }
}

private void checkElementIndex(int index) {
    if (index < 0 || index >= size) {
        throw new IndexOutOfBoundsException(
                "Index: " + index + ", Size: " + size
        );
    }
}

private void ensureNotEmpty() {
    if (isEmpty()) {
        throw new IllegalStateException("The linked list is empty.");
    }
}

// ==================== MAIN METHOD ====================

public static void main(String[] args) {
    LinkedList list = new LinkedList();

    list.addLast(1);
    list.addLast(2);
    list.addLast(3);
    list.addLast(2);
    list.addLast(1);

    System.out.println("Original list:");
    list.print();

    System.out.println("Size: " + list.size());
    System.out.println("Search 3: " + list.search(3));
    System.out.println("Middle: " + list.findMiddle());
    System.out.println("Is palindrome: " + list.isPalindrome());

    System.out.println("\nReverse:");
    list.reverse();
    list.print();

    System.out.println("\nMerge sort:");
    list.addLast(4);
    list.addFirst(0);
    list.mergeSort();
    list.print();

    System.out.println("\nRemove first: " + list.removeFirst());
    list.print();

    System.out.println("\nRemove last: " + list.removeLast());
    list.print();

    System.out.println("\nRemove index 1: " + list.remove(1));
    list.print();

    System.out.println("\nAdd 99 at index 1:");
    list.add(1, 99);
    list.print();

    System.out.println("\nRemove 2nd node from end:");
    System.out.println("Removed: " + list.removeNthFromEnd(2));
    list.print();

    System.out.println("\nZigzag:");
    list.zigzag();
    list.print();

    System.out.println("\nFinal size: " + list.size());
}
}
