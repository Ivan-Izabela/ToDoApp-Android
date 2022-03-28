import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:todoapp_non_nativ/ViewModel.dart';
import 'package:todoapp_non_nativ/domain/ToDoItem.dart';
import 'package:todoapp_non_nativ/screens/todoAdd.dart';
import 'package:todoapp_non_nativ/todo_widget.dart';

class ToDoList extends StatefulWidget{
  ToDoList();
  @override
  _ToDoState createState() =>_ToDoState();


}

class _ToDoState extends State<ToDoList>{
  _ToDoState();
  @override
  Widget build(BuildContext context) {
    List<ToDoItem> todos= Provider.of<ViewModel>(context).getAll();
    return Scaffold(
      appBar: AppBar(title: Text("ToDo List"),),
      body:
      Column(children: <Widget>[

        Expanded(child: ListView.builder(
            itemCount: todos.length,
            itemBuilder: (BuildContext context, int index){
              return ToDoWidget(todos[index]);
            }
        )),
        ElevatedButton(child: Text('New'),
          onPressed: () {

            Navigator.push(
              context,
              MaterialPageRoute(builder: (context) => TodoAdd()),
            );
          },),


        ]
      )

    );


  }
}