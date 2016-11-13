on run {para1}
set preKey to true
tell application "System Events"
    set processIsRunning to ((bundle identifier of processes) contains "com.microsoft.rdc.mac") 
end tell
if processIsRunning then
set preKey to false
end if
try
    tell application "Finder" to get application file id "com.microsoft.rdc.mac"
on error
    display alert "Please install Microsoft Remote Desktop from App Store"
	return 0
end try
tell application "Microsoft Remote Desktop"
    activate
    tell application "System Events"
	set frontmost of process "Microsoft Remote Desktop" to true
        tell process "Microsoft Remote Desktop"
if preKey then
	key code 36
end if
                        tell menu bar 1
                        tell menu "Window"
                                try
                                click menu item "Microsoft Remote Desktop"
                                end try
                        end tell
                        end tell
	if preKey then
		keystroke tab
	end if
		key code 115
		repeat para1 times
			key code 125   
		end repeat
		key code 36   
		return 0
	end tell
    end tell
end tell
end run
