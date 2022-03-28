import 'package:flutter/cupertino.dart';
import 'package:todoapp_non_nativ/domain/ToDoItem.dart';

class ViewModel with ChangeNotifier{
  List<ToDoItem> todos= <ToDoItem>[
    new ToDoItem(1, "lab1 ma", "idea project", "Completed", "12/12/2022"),
    new ToDoItem(2, "lab2 ma", "nativ", "NotStarted", "12/12/2022"),
    new ToDoItem(3, "lab3 ma", "non nativ", "Started", "12/12/2022")
  ];

  ViewModel();

  void addToDo(ToDoItem item){
    int id=0;
    for(int i =0;i<todos.length;i++){
      if(todos[i].id>id)
        id=todos[i].id;
    }
    item.id=id+1;
    todos.add(item);
    notifyListeners();
  }

  void deleteToDo(ToDoItem item){
    todos.removeWhere((D)=>D.id==item.id);
    notifyListeners();
  }

  void updateToDO(int id,ToDoItem item){
    for(int i =0;i<todos.length;i++){
      if(todos[i].id==id){
        todos[i]=item;
        break;}
    }
    notifyListeners();
  }

  List<ToDoItem> getAll()  =>todos;


}