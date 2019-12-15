```
1. Change Java file path in startDynamodb.cmd.
2. Open cmd in admin mode, and run startDynamodb.cmd
### If service create failed because of existing service name, try below to delete it.

sc delete local-dynamodb


3. Open Services and search for local-dynamodb
4. Try starting the service.
5. If started, mark it as Automatic (Delayed Start)
6. Click Ok and close services windows.
```