import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:todoapp_non_nativ/domain/ToDoItem.dart';

import '../ViewModel.dart';

class TodoEdit extends StatefulWidget{

  TodoEdit(this.item);
  final ToDoItem item;

  @override
  _ToDoEditState createState()=>_ToDoEditState();


}


class _ToDoEditState extends State<TodoEdit> {

  String title="";
  String description="";
  String status="NotStarted";
  String deadline="";
  String date = "";
  DateTime selectedDate = DateTime.now();
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    title=widget.item.title;
    description=widget.item.description;
    status=widget.item.state;
    deadline=widget.item.deadline;
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Edit ToDo"),),
      body: Column(
        children: <Widget>[
          Expanded(child:TextFormField(initialValue: title,decoration: InputDecoration(labelText: "Title"),onChanged: (text){title=text;},)),
          Expanded(child:TextFormField(initialValue: description,decoration: InputDecoration(labelText: "Description"),onChanged: (text){description=text;},)),
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
          ElevatedButton(child: Text('Save'),
            onPressed: () {
            ToDoItem item= ToDoItem(widget.item.id, title, description, status, deadline);
            Provider.of<ViewModel>(context,listen: false).updateToDO(widget.item.id,item);
              Navigator.pop(context);
            },),

          ElevatedButton(
                onPressed: () => showDialog<String>(
                  context: context,
                  builder: (BuildContext context) => AlertDialog(
                    title: const Text('Delete item'),
                    content: const Text('You want to delete this item?'),
                    actions: <Widget>[
                      TextButton(
                        onPressed: () {
                          Navigator.pop(context, 'Cancel');},
                        child: const Text('Cancel'),
                      ),
                      TextButton(
                        onPressed: () {
                          ToDoItem item= ToDoItem(widget.item.id, title, description, status, deadline);
                          Provider.of<ViewModel>(context,listen: false).deleteToDo(item);
                          Navigator.pop(context, 'Yes');
                          Navigator.pop(context, 'Yes');},
                        child: const Text('Yes'),
                      ),
                    ],
                  ),
                ),
                child: const Text('Delete'),
          ),







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



