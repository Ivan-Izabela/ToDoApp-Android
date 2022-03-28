import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:todoapp_non_nativ/ViewModel.dart';
import 'package:todoapp_non_nativ/screens/todoList.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider<ViewModel>(create: (_)=>ViewModel()),
      ],
      child: MaterialApp(
        title: 'ToDoApp',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: ToDoList(),
      ),
    );


  }
}


