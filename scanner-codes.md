# Scanner code documentation
Scanner request/responses are encoded in 64-byte string tokens beginning with a
four character code. All available codes are provided in the `utsushi` source
file `utsushi/drivers/esci/code-token.hpp` but their meaning appears to be
undocumented. This is a list of tokens with their inferred meanings from
examining the source code.

Tokens are grouped into namespaces (sections) in the source according to their
general function. The grouping has been preserved here.

## Request tokens

| Token | Definition                                        |
| ----- | ------------------------------------------------- |
| FIN   | Finished with device |
| CAN   | Cancel command |
| INFO  | Get scanner information |
| CAPA  | Get scanner capabilities |
| CAPB  | Get scanner capabilities, flip side only |
| PARA  | Get/set parameters |
| PARB  | Get/set parameters, flip side only |
| RESA  | Get/set resolution |
| RESB  | Get/set resolution, flip side only |
| STAT  | Get scanner status |
| MECH  | Send mechanical command (below) |
| TRDT  | Initiate scan, transition to data state (below) |
| IMG   | Acquire scanned image data |
| EXT0  | Transfer blobs |
| EXT1  | Transfer blobs |
| EXT2  | Transfer blobs |

## Response tokens

### Request Error
All responses are headed by the original request code received, unless an error
occurs: a `UNKN` response indicates an unknown request code, and `INVD`
indicates that a known request was received at an invalid time.

### Info

#### Types
Each token here may be followed by additional tokens specific to its type,
defined in another section below. 

| Token | Definition |
| ----- | ---------- |
| #ERR  | Hardware error |
| #NRD  | Not ready |
| #PST  | Page start, sent after first IMG request for an image |
| #PEN  | Page end, sent after image completes scanning |
| #LFT  | Images left to scan, if number of pages was specified using PAG |
| #TYP  | Side of page being scanned |
| #ATN  | Device attention |
| #PAR  | Scan parameter notification, in response to PARA, PARB, RESA, OR RESB request |
| #END  | End of response, is not necessarily present |

#### ERR tokens
Hardware location of error:

| Token | Definition |
| ----- | ---------- |
| ADF   | Automatic document feeder |
| TPU   | Transparency unit |
| FB    | Flatbed |

Nature of error:

| Token | Definition |
| ----- | ---------- |
| OPN   | |
| PJ    | Paper jam |
| PE    | |
| ERR   | Generic error |
| LOCK  | |
| DFED  | |
| DTCL  | |
| AUTH  | |
| PERM  | |
| BTLO  | |

#### NRD tokens

| Token | Definition |
| ----- | ---------- |
| RSVD  | |
| BUSY  | Device busy |
| WUP   | |
| NONE  | |

#### TYP tokens

| Token | Definition |
| ----- | ---------- |
| IMGA  | Front side |
| IMGB  | Back side |

#### ATN tokens

| Token | Definition |
| ----- | ---------- |
| CAN   | |
| NONE  | |

#### PAR tokens

| Token | Definition |
| ----- | ---------- |
| OK    | |
| FAIL  | |
| LOST  | |

## Value tokens

| Token | Definition |
| ----- | ---------- |
| LIST  | List of integers or tokens |
| RANG  | Range defined by two integers |

## Capability tokens
Todo

## Parameter tokens
Truncated

| Token | Definition |
| ----- | ---------- |
| #PAG  | Number of images to scan. Each duplex scan counts as two images |

## Mechanical tokens
Type of mechanical action:

| Token | Definition |
| ----- | ---------- |
| #ADF  | Automatic document feeder |
| #FCS  | Focus control |
| #INI  | |

ADF actions:

| Token | Definition |
| ----- | ---------- |
| LOAD  | Load sheet |
| EJECT | Eject sheet |
| CLEN  | Clean |
| CALB  | Calibrate/clean |

FCS actions:

| Token | Definition |
| ----- | ---------- |
| AUTO  | Autofocus |
| MANU  | Manual focus |

## Transition (TRDT)
The `TRDT` request initiates the scan, putting the scanner in "data state."
Only `FIN`, `CAN`, `IMG` and `EXT` requests can be sent in this state. The
scanner exits this state when images have been acquired, it receives a `FIN`
or `CAN` request, or an error occurs. When not in this state, `CAN` and `IMG`
requests are considered invalid.
