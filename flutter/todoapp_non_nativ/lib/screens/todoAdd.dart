import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:todoapp_non_nativ/domain/ToDoItem.dart';

import '../ViewModel.dart';

class TodoAdd extends StatefulWidget{

  TodoAdd();


  @override
  _ToDoAddState createState()=>_ToDoAddState();


}


class _ToDoAddState extends State<TodoAdd> {

  String title="";
  String description="";
  String status="NotStarted";
  String deadline="";
  String date = "";
  DateTime selectedDate = DateTime.now();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Edit ToDo"),),
      body: Column(
        children: <Widget>[
          Expanded(child:TextField(decoration: InputDecoration(labelText: "Title"),onChanged: (text){title=text;},)),
          Expanded(child:TextField(decoration: InputDecoration(labelText: "Description"),onChanged: (text){description=text;},)),

          DropdownButton(value:status,items: const[
            DropdownMenuItem(child: Text("NotStarted"), value: "NotStarted",),
            DropdownMenuItem(child: Text("Started"), value: "Started",),
            DropdownMenuItem(child: Text("Completed"), value: "Completed",),

          ],
            onChanged: (value){
              setState(() {
                status=value as String;
              });
            },),


          ElevatedButton(
            onPressed: () {
              _selectDate(context);
            },
            child: Text("Choose Dateline"),
          ),
          Text(deadline),
          ElevatedButton(child: Text('Add'),
            onPressed: () {
            if(title=="" || description=="" || deadline==""){
                  showDialog<String>(
                context: context,
                builder: (BuildContext context) => AlertDialog(
                  title: const Text('Invalid item'),
                  content: const Text('You must complete all fields!'),
                  actions: <Widget>[
                    TextButton(
                      onPressed: () {
                        Navigator.pop(context, 'Ok');},
                      child: const Text('Ok'),
                    ),

                  ],
                ),
              );
            }
            else{
              ToDoItem item= ToDoItem(-1, title, description, status, deadline);
              Provider.of<ViewModel>(context,listen: false).addToDo(item);
              Navigator.pop(context);}
            },),






        ],
      ),
    );
  }
  _selectDate(BuildContext context) async {
    final DateTime? selected = await showDatePicker(
      context: context,
      initialDate: selectedDate,
      firstDate: DateTime(2010),
      lastDate: DateTime(2025),
    );
    if (selected != null && selected != selectedDate)
      setState(() {
        selectedDate = selected;
        deadline=selectedDate.toString();
      });
  }
}



