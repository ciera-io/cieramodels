$TESTSCHEDULE

#RUN SCENARIO Integrity 1 "test01_base_test"
#RUN SCENARIO Integrity 2 "test02_duplicate_id"
#RUN SCENARIO Integrity 3 "test03_link_exists_one1"
#RUN SCENARIO Integrity 4 "test04_link_exists_one2"
#RUN SCENARIO Integrity 5 "test05_link_exists_many"
#RUN SCENARIO Integrity 6 "test06_unlink_no_link_one1"
#RUN SCENARIO Integrity 7 "test07_unlink_no_link_one2"
#RUN SCENARIO Integrity 8 "test08_unlink_no_link_many"
#RUN SCENARIO Integrity 9 "test09_inconsistent_refs"
#RUN SCENARIO Integrity 10 "test10_delete"

RUN SCENARIO Integrity 12 "run_test"

$ENDTESTSCHEDULE
