domain Integrity is

  object List;
  object Element;

  private service test01_base_test(); pragma scenario(1);
  private service test02_duplicate_id(); pragma scenario(2);
  private service test03_link_exists_one1(); pragma scenario(3);
  private service test04_link_exists_one2(); pragma scenario(4);
  private service test05_link_exists_many(); pragma scenario(5);
  private service test06_unlink_no_link_one1(); pragma scenario(6);
  private service test07_unlink_no_link_one2(); pragma scenario(7);
  private service test08_unlink_no_link_many(); pragma scenario(8);
  private service test09_inconsistent_refs(); pragma scenario(9);
  private service test10_delete(); pragma scenario(10);

  private service init(); pragma scenario(11); pragma startup();
  private service run_test(); pragma scenario(12);

  //! R1 establishes containment for the list. Each element instance must exist
  //! in exactly one list.
  relationship R1 is List conditionally contains many Element,
                     Element unconditionally belongs_to one List;

  //! R2 tracks the head of the queue. The first element in the list (if it
  //! exists) is accessible by the list itself
  relationship R2 is List conditionally has_first one Element,
                     Element conditionally is_first_in one List;

  //! R3 establishes order between adjacent elements and gives a convenient way
  //! to iterate the list in order
  relationship R3 is Element conditionally precedes one Element,
                     Element conditionally follows one Element;

  object List is

    name: preferred string;

    public instance service print();

  end object;

  object Element is

    list_name: preferred referential (R1.name, R2.name, R3.precedes.list_name) string;
    item: preferred string;
    next_item: referential (R3.precedes.item) string;

    public instance service print();

  end object;

end domain;
