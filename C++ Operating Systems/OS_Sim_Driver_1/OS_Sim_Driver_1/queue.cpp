// FILE: queue.cpp
// IMPLEMENTS: The ten functions of the linked list toolkit (see queue.h)

#include <assert.h>    // Provides assert
#include <stdlib.h>    // Provides NULL and int
//#include "link1.h"
#include "queue.h"

queue::queue(void)
{
}
queue::~queue(void)
{
}

int queue::list_length(Node* head_ptr)
// Library facilities used: stdlib.h
{
    Node *cursor;
    int answer;

    answer = 0;
    for (cursor = head_ptr; cursor != NULL; cursor = cursor->link)
        answer++;

    return answer;
}

void queue::list_head_insert(Node*& head_ptr, const Node::Item& entry)
{
    Node *insert_ptr;

    insert_ptr = new Node;
    insert_ptr->data = entry;
    insert_ptr->link = head_ptr;
    head_ptr = insert_ptr;
}

void queue::list_insert(Node* previous_ptr, const Node::Item& entry) 
{
    Node *insert_ptr;
    
    insert_ptr = new Node;
    insert_ptr->data = entry;
    insert_ptr->link = previous_ptr->link;
    previous_ptr->link = insert_ptr;
}

Node* queue::list_search(Node* head_ptr, const Node::Item& target) 
// Library facilities used: stdlib.h
{
    Node *cursor;
   
    for (cursor = head_ptr; cursor != NULL; cursor = cursor->link)
        if (target == cursor->data)
            return cursor;
    return NULL;
}

Node* queue::list_locate(Node* head_ptr, int position) 
// Library facilities used: assert.h, stdlib.h
{
    Node *cursor;
    int i;
    
//    assert (0 < position);
    cursor = head_ptr;
    for (i = 1; (i < position) && (cursor != NULL); i++)
        cursor = cursor->link;
    return cursor;
}

void queue::list_head_remove(Node*& head_ptr)
{
    Node *remove_ptr;

    remove_ptr = head_ptr;
    head_ptr = head_ptr->link;
    delete remove_ptr;
}

void queue::list_remove(Node* previous_ptr)
{
    Node *remove_ptr;

    remove_ptr = previous_ptr->link;
    previous_ptr->link = remove_ptr->link;
    delete remove_ptr;
}

void queue::list_clear(Node*& head_ptr)
// Library facilities used: stdlib.h
{
    while (head_ptr != NULL)
        list_head_remove(head_ptr);
}

void queue::list_copy(Node* source_ptr, Node*& head_ptr, Node*& tail_ptr) 
// Library facilities used: stdlib.h
{
    head_ptr = NULL;
    tail_ptr = NULL;

    // Handle the case of the empty list
    if (source_ptr == NULL)
        return;
    
    // Make the head node for the newly created list, and put data in it
    list_head_insert(head_ptr, source_ptr->data);
    tail_ptr = head_ptr;
    
    // Copy the rest of the nodes one at a time, adding at the tail of new list
    for (source_ptr = source_ptr->link; source_ptr != NULL; source_ptr = source_ptr->link)
    {
        list_insert(tail_ptr, source_ptr->data);
        tail_ptr = tail_ptr->link;
    }
}

void queue::list_piece(Node* start_ptr, Node* end_ptr, Node*& head_ptr, Node*& tail_ptr)
// Library facilities used: stdlib.h
{
    head_ptr = NULL;
    tail_ptr = NULL;

    // Handle the case of the empty list
    if (start_ptr == NULL)
        return;
    
    // Make the head node for the newly created list, and put data in it
    list_head_insert(head_ptr, start_ptr->data);
    tail_ptr = head_ptr;
    if (start_ptr == end_ptr)
        return;
    
    // Copy the rest of the nodes one at a time, adding at the tail of new list
    for (start_ptr = start_ptr->link; start_ptr != NULL; start_ptr = start_ptr->link)
    {
        list_insert(tail_ptr, start_ptr->data);
        tail_ptr = tail_ptr->link;
        if (start_ptr == end_ptr) 
            return;
    }
}

