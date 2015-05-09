// FILE: link1.h
// PROVIDES: A toolkit of ten functions for manipulating linked lists. Each
// node of the list contains a piece of data and a pointer to the next node.
// The type of the data is defined as Node::Item in a typedef statement.
// The complete type definitions used by the toolkit are:
//
//   struct Node                   Item may be any of the C++ built-in types
//   {                             (int, char, etc.), or a class with a default
//       typedef _____ Item;       constructor, an assignment operator,
//       Item data;                and a test for equality (x == y).
//       Node *link;
//   };
//
// FUNCTIONS in the linked list toolkit:
//   int list_length(Node* head_ptr)
//     Precondition: head_ptr is the head pointer of a linked list.
//     Postcondition: The value returned is the number of nodes in the linked
//     list. The list itself is unaltered.
//
//   void list_head_insert(Node*& head_ptr, const Node::Item& entry) 
//     Precondition: head_ptr is the head pointer of a linked list.
//     Postcondition: A new node containing the given entry has been added at
//     the head of the linked list; head_ptr now points to the head of the new,
//     longer linked list.
//
//   void list_insert(Node* previous_ptr, const Node::Item& entry) 
//     Precondition: previous_ptr points to a node in a linked list.
//     Postcondition: A new node containing the given entry has been added
//     after the node that previous_ptr points to.
//
//   Node* list_search(Node* head_ptr, const Node::Item& target) 
//     Precondition: head_ptr is the head pointer of a linked list.
//     Postcondition: The pointer returned points to the first node containing
//     the specified target in its data member. If there is no such node, the
//     null pointer is returned. The list itself is unaltered.
//
//   Node* list_locate(Node* head_ptr, int position) 
//     Precondition: head_ptr is the head pointer of a linked list, and
//     position > 0.
//     Postcondition: The pointer returned points to the node at the specified
//     position in the list. (The head node is position 1, the next node is
//     position 2, and so on). If there is no such position, then the null
//     pointer is returned. The list itself is unaltered.
//
//   void list_head_remove(Node*& head_ptr) 
//     Precondition: head_ptr is the head pointer of a linked list, with at
//     least one node.
//     Postcondition: The head node has been removed and returned to the heap;
//     head_ptr is now the head pointer of the new, shorter linked list.
//
//   void list_remove(Node* previous_ptr) 
//     Precondition: previous_ptr points to a node in a linked list, and this
//     is not the tail node of the list.
//     Postcondition: The node after previous_ptr has been removed from the
//     linked list.
//
//   void list_clear(Node*& head_ptr) 
//     Precondition: head_ptr is the head pointer of a linked list.
//     Postcondition: All nodes of the list have been returned to the heap,
//     and the head_ptr is now NULL.
//
//   void list_copy(Node* source_ptr, Node*& head_ptr, Node*& tail_ptr) 
//     Precondition: source_ptr is the head pointer of a linked list.
//     Postcondition: head_ptr and tail_ptr are the head and tail pointers for
//     a new list that contains the same items as the list pointed to by
//     source_ptr. The original list is unaltered.
//
//   void list_piece
//     Node* start_ptr, Node* end_ptr, Node*& head_ptr, Node*& tail_ptr) 
//     Precondition: start_ptr and end_ptr are pointers to nodes on the same
//     linked list, with the start_ptr node at or before the end_ptr node.
//     Postcondition: head_ptr and tail_ptr are the head and tail pointers for
//     a new linked list that contains the items from start_ptr to end_ptr.
//     The original list is unaltered.
//    
// DYNAMIC MEMORY usage by the toolkit: 
//   If there is insufficient dynamic memory, then the following functions call
//   new_handler before any changes are made to the list that head_ptr points
//   to : list_head_insert, list_insert, list_copy, list_piece.

#ifndef LINK1_H  
#define LINK1_H
#include <stdlib.h> // Provides int
#include "pcb.h"

struct Node
{
    typedef int Item;
    Item data;
    Node *link;
};

class queue
{
public:
	queue(void);
	~queue(void);

/*	struct term
	{
		char var;
		int coeff;
		unsigned int exp;
		term () {}; // only purpose is  to facilitate array declaration.
		term (char v,int c, unsigned int e) : var(v),coeff(c), exp(e) {};
		char sign () const {return ((coeff<0)?'-':((coeff==0)?'0':'+'));};
		bool operator== (const term & t2) const
			{return ((var==t2.var)&&(coeff == t2.coeff) && (exp == t2.exp));};
	};
*/


    // FUNCTIONS for the linked list toolkit
    int list_length(Node* head_ptr);
    void list_head_insert(Node*& head_ptr, const Node::Item& entry); 
    void list_insert(Node* previous_ptr, const Node::Item& entry);  
    Node* list_search(Node* head_ptr, const Node::Item& target);
    Node* list_locate(Node* head_ptr, int position);
    void list_head_remove(Node*& head_ptr);
    void list_remove(Node* previous_ptr);
    void list_clear(Node*& head_ptr);
    void list_copy(Node* source_ptr, Node*& head_ptr, Node*& tail_ptr);
    void list_piece(Node* start_ptr, Node* end_ptr, Node*& head_ptr, Node*& tail_ptr);
	
};
#endif

